package ru.practicum.shareit.item.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 50)
    private String description;
    @NotNull
    private Boolean available;
    private ItemRequest request;//ссылка на запрос пользователя о создании
}