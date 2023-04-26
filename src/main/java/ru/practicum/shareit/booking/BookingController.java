package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constans.State;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID = "X-Sharer-User-Id";
    private final UserService userService;

    @PostMapping
    public Booking saveBooking(@RequestHeader(value = USER_ID) Long userId, @Valid @RequestBody Booking booking) {
        checkUser(userId);
        return bookingService.saveBooking(booking);
    }

    @PatchMapping("/{bookingId}")
    public Booking confirmBooking(@RequestHeader(value = USER_ID) Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        checkUser(userId);
        return bookingService.confirmBooking(approved, bookingId, userId);
    }

    @PostMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader(value = USER_ID) Long userId, @PathVariable Long bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public Collection<Booking> getAllBooking(@RequestHeader(value = USER_ID) Long userId, @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getAllBooking(userId, state);
    }

    private void checkUser(Long userId) {
        if (userId == null) {
            throw new NotFoundException("Пользователь не указан");
        } else if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Указанный пользователь не сущетсвует");
        }
    }
}