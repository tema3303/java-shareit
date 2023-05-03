package ru.practicum.shareit.request.model.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestDtoIn {
    private long id;
    @NotBlank
    private String description;
    private Long userId;
    private LocalDateTime created;
}