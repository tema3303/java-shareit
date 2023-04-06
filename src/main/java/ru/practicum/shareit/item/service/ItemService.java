package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item addItem(Item item);

    Item updateItem(Item item, long itemId);

    Item getItemById(long itemId);

    Collection<Item> getAllItem(long userId);

    Collection<Item> searchItem(String text);
}