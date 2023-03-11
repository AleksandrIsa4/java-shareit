package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingMessageDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
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
class BookingServiceTest {

    @Mock
    UserService userService;
    @Mock
    BookingService bookingService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemRepository itemRepository;
    BookingResponseDto bookingResponseDto1;
    BookingMessageDto bookingMessageDto1;
    BookingResponseDto bookingResponseDto2;
    BookingMessageDto bookingMessageDto2;
    ItemResponseDto itemResponseDto2;
    ItemResponseDto itemResponseDto1;
    Item item1;
    Item item2;
    User user1;
    User user2;
    Booking booking1;
    Booking booking2;

    @BeforeEach
    void setUp() {
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
        booking1 = new Booking(item1, user2, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), Status.APPROVED);
        booking1.setId(1L);
        booking2 = new Booking(item2, user2, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), Status.APPROVED);
        booking2.setId(2L);
        bookingResponseDto1 = new BookingResponseDto(itemResponseDto1, user2, LocalDateTime.now().plusSeconds(5), LocalDateTime.now().plusSeconds(6), Status.WAITING, 2L);
        bookingResponseDto2 = new BookingResponseDto(itemResponseDto2, user2, LocalDateTime.now().plusSeconds(5), LocalDateTime.now().plusSeconds(6), Status.APPROVED, 2L);
        bookingMessageDto1 = new BookingMessageDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2));
        bookingMessageDto2 = new BookingMessageDto(2L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2));
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
    }
}