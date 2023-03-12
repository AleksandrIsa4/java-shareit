package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.State;
import ru.practicum.shareit.booking.model.enumeration.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookingServiceTest {

    @Mock
    UserService userService;
    @Mock
    BookingService bookingService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemRepository itemRepository;
    ItemResponseDto itemResponseDto2;
    ItemResponseDto itemResponseDto1;
    Item item1;
    Item item2;
    User user1;
    User user2;
    Booking booking1;
    Booking booking2;

    @BeforeEach
    void init() {
        bookingService = new BookingService(bookingRepository,
                itemRepository,
                userService);
        itemResponseDto1 = new ItemResponseDto("item1", "description Item1", true, null, null, null, null);
        itemResponseDto2 = new ItemResponseDto("item2", "description Item2", false, null, null, null, null);
        itemResponseDto1.setId(1L);
        itemResponseDto2.setId(2L);
        user1 = new User("NameTest", "test@test.ru");
        user1.setId(1L);
        user2 = new User("NameTest2", "test@test2.ru");
        user2.setId(2L);
        item1 = new Item("item1", "description Item1", true, user1, null);
        item2 = new Item("item2", "description Item2", false, user1, null);
        item1.setId(1L);
        item2.setId(2L);
        booking1 = new Booking(item1, user2, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), Status.WAITING);
        booking1.setId(1L);
        booking2 = new Booking(item2, user2, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), Status.APPROVED);
        booking2.setId(2L);
    }

    @Test
    void save() {
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user1);
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking1);
        Booking bookingTest = bookingService.save(booking1, 2L, 1L);
        Assertions.assertEquals(bookingTest, booking1);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            bookingService.save(booking1, 1L, 1L);
        });
    }

    @Test
    void patch() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user1);
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking1));
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking1);
        Booking bookingTest = bookingService.patch(true, 1L, 1L);
        Assertions.assertEquals(bookingTest, booking1);
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking1));
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> bookingService.patch(true, 1L, 2L));
        Assertions.assertEquals("404 NOT_FOUND \"Пользователь не владелец\"", exception.getMessage());
    }

    @Test
    void patchWrong() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user1);
        booking1.setStatus(Status.REJECTED);
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking1));
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> bookingService.patch(false, 1L, 1L));
        Assertions.assertEquals("400 BAD_REQUEST \"Статус предмета уже изменен\"", exception.getMessage());
    }

    @Test
    void get() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking1));
        Booking bookingTest = bookingService.get(1L, 1L);
        Assertions.assertEquals(bookingTest, booking1);
    }

    @Test
    void getAllBooker() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user2);
        Mockito.when(bookingRepository.findAllByBookerIdOrderByStartDesc(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest = bookingService.getAllBooker(1L, State.ALL, 0, 9);
        List<Booking> bookingList = List.of(booking1);
        Assertions.assertEquals(bookingTest, bookingList);
        Mockito.when(bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest2 = bookingService.getAllBooker(1L, State.CURRENT, 0, 9);
        Assertions.assertEquals(bookingTest2, bookingList);
        Mockito.when(bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest3 = bookingService.getAllBooker(1L, State.PAST, 0, 9);
        Assertions.assertEquals(bookingTest3, bookingList);
        Mockito.when(bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest4 = bookingService.getAllBooker(1L, State.FUTURE, 0, 9);
        Assertions.assertEquals(bookingTest4, bookingList);
        Mockito.when(bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(Mockito.anyLong(), Mockito.any(Status.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest5 = bookingService.getAllBooker(1L, State.WAITING, 0, 9);
        Assertions.assertEquals(bookingTest5, bookingList);
        Mockito.when(bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(Mockito.anyLong(), Mockito.any(Status.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest6 = bookingService.getAllBooker(1L, State.REJECTED, 0, 9);
        Assertions.assertEquals(bookingTest6, bookingList);
    }

    @Test
    void getAllOwner() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user1);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest = bookingService.getAllOwner(2L, State.CURRENT, 0, 9);
        List<Booking> bookingList = List.of(booking1);
        Assertions.assertEquals(bookingTest, bookingList);
        Mockito.when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest2 = bookingService.getAllOwner(2L, State.ALL, 0, 9);
        Assertions.assertEquals(bookingTest2, bookingList);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest3 = bookingService.getAllOwner(2L, State.PAST, 0, 9);
        Assertions.assertEquals(bookingTest3, bookingList);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest4 = bookingService.getAllOwner(2L, State.FUTURE, 0, 9);
        Assertions.assertEquals(bookingTest4, bookingList);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStatusIsOrderByStartDesc(Mockito.anyLong(), Mockito.any(Status.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest5 = bookingService.getAllOwner(2L, State.WAITING, 0, 9);
        Assertions.assertEquals(bookingTest5, bookingList);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStatusIsOrderByStartDesc(Mockito.anyLong(), Mockito.any(Status.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking1));
        List<Booking> bookingTest6 = bookingService.getAllOwner(2L, State.REJECTED, 0, 9);
        Assertions.assertEquals(bookingTest6, bookingList);
    }
}