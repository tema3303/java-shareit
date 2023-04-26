package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.constans.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    private long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private User booker; //кто бронирует
    private Status status;
}