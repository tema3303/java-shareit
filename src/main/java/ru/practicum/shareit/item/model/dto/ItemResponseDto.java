package ru.practicum.shareit.item.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class ItemResponseDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;//ссылка на запрос пользователя о создании
}
