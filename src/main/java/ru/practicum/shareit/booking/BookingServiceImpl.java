package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.constans.State;
import ru.practicum.shareit.constans.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking confirmBooking(Boolean approve, Long id, Long userId) {
        Booking booking = getBookingById(id, userId);
        Item item = booking.getItem();
        if (item.getOwner().getId() != userId) {
            throw new ValidationException("Данный пользователь не хозяин вещи");
        }
        if (approve == true) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return booking;
    }

    @Override
    public Booking getBookingById(Long id, Long userId) {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Нет данных о бронировании"));
    }

    @Override
    public Collection<Booking> getAllBooking(Long userId, State state) {
        Collection<Booking> bookings = null;
        if (state == State.ALL) {
            bookings = bookingRepository.findAllByBookerIdOrderByStart(userId);
        } else if (state == State.WAITING) {
            bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, state);
        } else if (state == State.REJECTED) {
            bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, state);
        } else if (state == State.CURRENT) {
            bookings = bookingRepository.findAllByBookerIdAndStartAfterAndEndBefore(userId, LocalDateTime.now(), LocalDateTime.now());
        } else if (state == State.FUTURE) {
            bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStart(userId, LocalDateTime.now());
        } else if (state == State.PAST) {
            bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStart(userId, LocalDateTime.now());
        }
        return bookings;
    }

    @Override
    public Collection<Booking> getAllBookingForItems(Long userId, State state) {
        return null;
    }
}