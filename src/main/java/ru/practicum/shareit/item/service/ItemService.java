package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemBookingDto;

import java.util.Collection;

public interface ItemService {
    Item addItem(Item item);

    Item updateItem(Item item, long itemId);

    ItemBookingDto getItemById(long id, long userId);

    Collection<ItemBookingDto> getAllItem(long userId);

    Collection<Item> searchItem(String text);

    CommentDto addComment(Long authorId, Long itemId, CommentOutDto commentOutDto);
}