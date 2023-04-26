package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.constans.State;
import ru.practicum.shareit.constans.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findAllByBookerIdAndStatusOrderByStart(Long userId, State state);

    Collection<Booking> findAllByBookerIdOrderByStart(Long userId);

    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByStart(Long userId, LocalDateTime time);

    Collection<Booking> findAllByBookerIdAndStartAfterOrderByStart(Long userId, LocalDateTime time);

    Collection<Booking> findAllByBookerIdAndStartAfterAndEndBefore(Long userId, LocalDateTime time, LocalDateTime currentTime);

}