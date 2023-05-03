package ru.practicum.shareit.items;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.constans.Status;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemBookingDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    private static final User myUser = User.builder()
            .id(1L)
            .email("artem@yandex.com")
            .name("Artem")
            .build();

    private static final User otherUser = User.builder()
            .id(2L)
            .email("mark@gmail.com")
            .name("Mark")
            .build();

    private static final Item myItem = Item.builder()
            .id(1L)
            .name("Item")
            .owner(myUser)
            .description("Description item")
            .available(true)
            .build();

    private static final Comment comment = Comment.builder()
            .id(1L)
            .item(myItem)
            .author(otherUser)
            .text("Cool!")
            .build();

    private static final Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().minusDays(2))
            .end(LocalDateTime.now())
            .item(myItem)
            .booker(otherUser)
            .build();

    @Test
    void addItem() {
        when(userRepository.findById(myUser.getId()))
                .thenReturn(Optional.of(myUser));
        when(itemRepository.save(myItem))
                .thenReturn(myItem);
        ItemDto itemDto = itemService.addItem(ItemMapper.toItemDto(myItem), myUser.getId());
        assertEquals(itemDto, ItemMapper.toItemDto(myItem));
        verify(itemRepository).save(myItem);
    }

    @Test
    void updateItemReturn() {
        ItemDto itemUp = ItemDto.builder()
                .name("Updated name")
                .available(false)
                .build();
        when(userRepository.findById(myUser.getId()))
                .thenReturn(Optional.of(myUser));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(myItem));
        when(itemRepository.save(any(Item.class))).then(AdditionalAnswers.returnsFirstArg());
        ItemDto itemUpdate = itemService.updateItem(itemUp, 1L, myUser.getId());
        assertThat(itemUpdate.getId()).isEqualTo(1);
        assertThat(itemUpdate.getName()).isEqualTo("Updated name");
        assertThat(itemUpdate.getDescription()).isEqualTo("Description item");
        assertThat(itemUpdate.getAvailable()).isFalse();
        verify(itemRepository).findById(anyLong());
    }

    @Test
    void getItemById() {
        when(itemRepository.findById(myItem.getId())).thenReturn(Optional.of(myItem));
        when(bookingRepository
                .findAllByItem_IdAndItem_Owner_IdAndStatusNotOrderByStartAsc(
                        myItem.getId(), myUser.getId(), Status.REJECTED
                )).thenReturn(List.of(booking));
        when(commentRepository.findAllByItem_IdOrderByCreated(myItem.getId()))
                .thenReturn(List.of(comment));

        ItemBookingDto itemBookingDto = itemService.getItemById(myItem.getId(), myUser.getId());
        CommentDto commentDto = CommentMapper.toDto(comment);

        assertEquals(itemBookingDto, ItemMapper.toItemBookingDto(myItem, List.of(commentDto), booking, null));
    }
}