package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.constans.State;
import ru.practicum.shareit.constans.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnsupportedException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto saveBooking(BookingDtoIn booking, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя не существует"));
        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(() -> new NotFoundException("Предмета не существует"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна к бронированию");
        }
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd())) {
            throw new ValidationException("Вещь не доступна к бронированию");
        }
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("Вещь не может быть забронирована, так как пользователь хояин вещи");
        }
        Booking bookingIn = BookingMapper.toBooking(booking, user, item);
        return BookingMapper.toBookingDto(bookingRepository.save(bookingIn));
    }

    @Override
    public BookingDto confirmBooking(Boolean approve, Long id, Long userId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Нет данных о бронировании"));
        Item item = booking.getItem();
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Данный пользователь не хозяин вещи");
        }
        if (booking.getStatus() == Status.APPROVED) {
            throw new ValidationException("Статус уже APPROVED");
        }
        if (approve == true) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Нет данных о бронировании"));
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotFoundException("Данный пользователь не обладает доступом к просмотру");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingDto> getAllBooking(Long userId, State state) {
        Collection<Booking> bookings;
        LocalDateTime time = LocalDateTime.now();
        if (state == State.ALL) {
            bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        } else if (state == State.WAITING) {
            bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
        } else if (state == State.REJECTED) {
            bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
        } else if (state == State.CURRENT) {
            bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndIsAfterOrderByStartDesc(userId, time, time);
        } else if (state == State.FUTURE) {
            bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, time);
        } else if (state == State.PAST) {
            bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, time);
        } else {
            throw new UnsupportedException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingDto> getAllBookingForItems(Long userId, State state) {
        Collection<Booking> bookings;
        LocalDateTime time = LocalDateTime.now();
        if (state == State.ALL) {
            bookings = bookingRepository.findAllByItem_OwnerIdOrderByStartDesc(userId);
        } else if (state == State.REJECTED) {
            bookings = bookingRepository.findAllByItem_OwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
        } else if (state == State.WAITING) {
            bookings = bookingRepository.findAllByItem_OwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
        } else if (state == State.CURRENT) {
            bookings = bookingRepository.findAllByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, time, time);
        } else if (state == State.FUTURE) {
            bookings = bookingRepository.findAllByItem_OwnerIdAndStartAfterOrderByStartDesc(userId, time);
        } else if (state == State.PAST) {
            bookings = bookingRepository.findAllByItem_OwnerIdAndEndBeforeOrderByStartDesc(userId, time);
        } else {
            throw new UnsupportedException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}