package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private long id; //id запроса
    private String description; //текст запрса
    private User requestor; //кто запрашивает
    private LocalDate created;
}
