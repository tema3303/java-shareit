package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.constans.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, Status status);//waiting and rejected

    Collection<Booking> findAllByBookerIdOrderByStartDesc(Long userId);//для ALL

    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime time);//past

    Collection<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime time);//future

    Collection<Booking> findAllByBookerIdAndStartBeforeAndEndIsAfterOrderByStartDesc(Long userId, LocalDateTime time,
                                                                                     LocalDateTime currentTime); //для Current

    Collection<Booking> findAllByItem_OwnerIdOrderByStartDesc(Long ownerId);//all

    Collection<Booking> findAllByItem_OwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status);//waiting and rejected

    Collection<Booking> findAllByItem_OwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime time);//past

    Collection<Booking> findAllByItem_OwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime time);//future

    Collection<Booking> findAllByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, LocalDateTime time,
                                                                                       LocalDateTime currentTime);//all

    Collection<Booking> findAllByItem_IdAndItem_Owner_IdAndStatusNotOrderByStartAsc(Long itemId, Long userId, Status status);

    Collection<Booking> findAllByItem_IdInOrderByStartAsc(Set<Long> itemsId);

    Collection<Booking> findAllByItem_IdAndBooker_IdAndStatusNotAndStartBefore(Long itemId, Long userId, Status status, LocalDateTime time);
}