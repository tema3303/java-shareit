package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestHeader(value = USER_ID) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос 'POST /items'");
        checkUser(userId);
        User user = userService.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemService.addItem(item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = USER_ID) Long userId, @RequestBody ItemDto itemDto,
                              @PathVariable long itemId) {
        log.info(String.format("Получен запрос 'PATCH /items/%d'", itemId));
        checkUser(userId);
        if (itemService.getItemById(itemId).getOwner().getId() != userId) {
            throw new NotFoundException("Пользователь не является владельцем этой вещи");
        }
        User user = userService.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemService.updateItem(item, itemId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(value = USER_ID) Long userId, @PathVariable Long itemId) {
        log.info(String.format("Получен запрос 'GET /items/%d'", itemId));
        checkUser(userId);
        return ItemMapper.toItemDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public Collection<ItemDto> getAllItem(@RequestHeader(value = USER_ID) Long userId) {
        log.info(String.format("Получен запрос 'GET /items' от пользователя %d'", userId));
        checkUser(userId);
        Collection<Item> allItems = itemService.getAllItem(userId);
        return allItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestHeader(value = USER_ID) Long userId, @RequestParam(name = "text") String text) {
        log.info("Получен запрос 'GET /items/search/?text='" + text);
        checkUser(userId);
        return itemService.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkUser(Long userId) {
        if (userId == null) {
            throw new NotFoundException("Пользователь не указан");
        } else if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Указанный пользователь не сущетсвует");
        }
    }
}