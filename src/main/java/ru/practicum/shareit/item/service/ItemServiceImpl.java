package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item addItem(Item item) {
        return itemRepository.addItem(item);
    }

    @Override
    public Item updateItem(Item item, long itemId) {
        return itemRepository.updateItem(item, itemId);
    }

    @Override
    public Item getItemById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public Collection<Item> getAllItem(long userId) {
        return itemRepository.getAllItem(userId);
    }

    @Override
    public Collection<Item> searchItem(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItem(text);
    }
}