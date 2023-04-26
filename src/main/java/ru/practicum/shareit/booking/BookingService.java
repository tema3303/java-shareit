package ru.practicum.shareit.booking;

import ru.practicum.shareit.constans.State;

import java.util.Collection;

public interface BookingService {
    Booking saveBooking(Booking booking);

    Booking confirmBooking(Boolean approve, Long id, Long userId);

    Booking getBookingById(Long id, Long userId);

    Collection<Booking> getAllBooking(Long userId, State state);

    Collection<Booking> getAllBookingForItems(Long userId, State state);
}