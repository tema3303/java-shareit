package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(value = USER_ID) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос 'POST /items'");
        ;
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(value = USER_ID) Long userId, @RequestBody ItemDto itemDto,
                                             @PathVariable long itemId) {
        log.info(String.format("Получен запрос 'PATCH /items/%d'", itemId));
        return itemClient.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(value = USER_ID) Long userId, @PathVariable Long itemId) {
        log.info(String.format("Получен запрос 'GET /items/%d'", itemId));
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItem(@RequestHeader(value = USER_ID) Long userId) {
        log.info(String.format("Получен запрос 'GET /items' от пользователя %d'", userId));
        return itemClient.getAllItem(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(value = USER_ID) Long userId, @RequestParam(name = "text") String text) {
        log.info("Получен запрос 'GET /items/search/?text='" + text);
        return itemClient.searchItem(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader(value = USER_ID) Long userId, @PathVariable Long itemId, @RequestBody CommentOutDto comment) {
        return itemClient.addComment(userId, itemId, comment);
    }
}