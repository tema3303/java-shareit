package ru.practicum.shareit.comments;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class Comment {
    private long id;
    private String text;
    private Item item;
    private User author; //кто бронирует
}