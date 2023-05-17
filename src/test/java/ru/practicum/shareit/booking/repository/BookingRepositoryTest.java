package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {

    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {

    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndStartAfterOrderByStartDesc() {

    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndIsAfterOrderByStartDesc() {

    }

    @Test
    void findAllByItem_OwnerIdOrderByStartDesc() {
    }

    @Test
    void findAllByItem_OwnerIdAndStatusOrderByStartDesc() {

    }

    @Test
    void findAllByItem_OwnerIdAndEndBeforeOrderByStartDesc() {

    }

    @Test
    void findAllByItem_OwnerIdAndStartAfterOrderByStartDesc() {

    }

    @Test
    void findAllByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {

    }

    @Test
    void findAllByItem_IdAndItem_Owner_IdAndStatusNotOrderByStartAsc() {

    }

    @Test
    void findAllByItem_IdInOrderByStartAsc() {

    }

    @Test
    void findAllByItem_IdAndBooker_IdAndStatusNotAndStartBefore() {

    }
}