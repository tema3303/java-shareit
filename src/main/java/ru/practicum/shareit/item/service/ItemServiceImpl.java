package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
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
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(Item item, long itemId) {
        Item updateItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмета не существует"));
        //updateItem.setId(itemId);
        if (item.getOwner() != null) {
            updateItem.setOwner(item.getOwner());
        }
        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        return itemRepository.save(updateItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItemById(long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Предмета не существует"));
        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Item> getAllItem(long userId) {
        return itemRepository.findAllByOwnerId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Item> searchItem(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text);
    }
}