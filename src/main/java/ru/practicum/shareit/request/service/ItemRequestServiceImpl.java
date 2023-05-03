package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.dto.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addItemRequest(ItemRequestDtoIn itemRequestDtoIn, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя не существует"));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDtoIn, user, LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDtoOut> getAllOwnRequest(Long userId) {
        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequester_Id(userId);
        Collection<ItemDto> items = itemRepository.findAllByRequestId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return itemRequests.stream()
                .map(item -> {
                    List<ItemDto> responsesForCurrentRequest = items.stream()
                            .filter(res -> Objects.equals(res.getRequestId(), item.getId()))
                            .collect(Collectors.toList());
                    return ItemRequestMapper.toItemRequestDtoOut(item, responsesForCurrentRequest);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDtoOut> getAllRequests(Long userId, Integer from, Integer size) {
        Collection<ItemRequest> requests;
        if (from != null && size != null) {
            if (from < 0 || size < 0) {
                throw new ValidationException("Значения не могут быть отрицательными");
            }
            int pageNumber = from / size;
            Pageable pagination = PageRequest.of(pageNumber, size);
            Page<ItemRequest> pageItemRequests = itemRequestRepository.findAllByRequester_IdNot(userId, pagination);
            requests = pageItemRequests.getContent();
        } else {
            requests = itemRequestRepository.findAllByRequester_IdNot(userId);
        }
        Collection<ItemDto> items = itemRepository.findAllByRequestIdNot(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return requests.stream()
                .map(item -> {
                    List<ItemDto> responsesForCurrentRequest = items.stream()
                            .filter(res -> Objects.equals(res.getRequestId(), item.getId()))
                            .collect(Collectors.toList());
                    return ItemRequestMapper.toItemRequestDtoOut(item, responsesForCurrentRequest);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDtoOut getRequestById(Long userId, Long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Не существует запроса с данным id"));
        List<ItemDto> items = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return ItemRequestMapper.toItemRequestDtoOut(request, items);
    }
}