package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.constans.State;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID = "X-Sharer-User-Id";
    private final UserService userService;

    @PostMapping
    public BookingDto saveBooking(@RequestHeader(value = USER_ID) Long userId, @Valid @RequestBody BookingDtoIn booking) {
        log.info("Получен запрос 'POST /bookings'");
        checkUser(userId);
        return bookingService.saveBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmBooking(@RequestHeader(value = USER_ID) Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        log.info("Получен запрос 'Patch /bookings/{bookingId}'");
        checkUser(userId);
        return bookingService.confirmBooking(approved, bookingId, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(value = USER_ID) Long userId, @PathVariable Long bookingId) {
        log.info("Получен запрос 'Get /bookings/{bookingId}'");
        checkUser(userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> getAllBooking(@RequestHeader(value = USER_ID) Long userId,
                                                @RequestParam(defaultValue = "ALL") State state,
                                                @RequestParam(required = false) Integer from,
                                                @RequestParam(required = false) Integer size) {
        log.info("Получен запрос 'Get /bookings'");
        checkUser(userId);
        return bookingService.getAllBooking(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllBookingForItems(@RequestHeader(value = USER_ID) Long userId,
                                                        @RequestParam(defaultValue = "ALL") State state,
                                                        @RequestParam(required = false) Integer from,
                                                        @RequestParam(required = false) Integer size) {
        log.info("Получен запрос 'Get /bookings/owner'");
        checkUser(userId);
        return bookingService.getAllBookingForItems(userId, state, from, size);
    }

    private void checkUser(Long userId) {
        if (userId == null) {
            throw new NotFoundException("Пользователь не указан");
        } else if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Указанный пользователь не сущетсвует");
        }
    }
}