package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private static final String USER_ID = "X-Sharer-User-Id";
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(value = USER_ID) Long userId, @Valid @RequestBody ItemRequestDtoIn itemRequestDto) {
        log.info("Получен запрос 'Post /requests'");
        checkUser(userId);
        return itemRequestService.addItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public Collection<ItemRequestDtoOut> getAllOwnRequest(@RequestHeader(value = USER_ID) Long userId) {
        log.info("Получен запрос 'Get /requests'");
        checkUser(userId);
        return itemRequestService.getAllOwnRequest(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAllRequests(@RequestHeader(value = USER_ID) Long userId,
                                                  @RequestParam(required = false) Integer from,
                                                  @RequestParam(required = false) Integer size) {
        log.info("Получен запрос 'Get /requests/all'");
        checkUser(userId);
        return itemRequestService.getAllRequests(userId, from, size);
    }


    @GetMapping("/{requestId}")
    public ItemRequestDtoOut getRequestById(@RequestHeader(value = USER_ID) Long userId, @PathVariable Long requestId) {
        log.info("Получен запрос 'Get /requests/{requestId}'");
        checkUser(userId);
        return itemRequestService.getRequestById(userId, requestId);
    }

    private void checkUser(Long userId) {
        if (userId == null) {
            throw new NotFoundException("Пользователь не указан");
        } else if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Указанный пользователь не сущетсвует");
        }
    }
}