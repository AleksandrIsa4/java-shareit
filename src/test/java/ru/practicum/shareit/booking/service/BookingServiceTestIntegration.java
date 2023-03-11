package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingMessageDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.State;
import ru.practicum.shareit.booking.model.enumeration.Status;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTestIntegration {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    BookingResponseDto bookingResponseDto1;
    BookingMessageDto bookingMessageDto1;
    BookingResponseDto bookingResponseDto2;
    BookingMessageDto bookingMessageDto2;
    ItemResponseDto itemResponseDto2;
    ItemResponseDto itemResponseDto1;
    ItemMessageDto itemMessageDto1;
    ItemMessageDto itemMessageDto2;
    Item item1;
    Item item2;
    User user1;
    User user2;
    Booking booking1;
    Booking booking2;

    @BeforeEach
    void setUp() {
        itemResponseDto1 = new ItemResponseDto("item1", "description Item1", true, null, null, null, null);
        itemResponseDto2 = new ItemResponseDto("item2", "description Item2", false, null, null, null, null);
        itemMessageDto1 = new ItemMessageDto("item1", "description Item1", true, null);
        itemMessageDto2 = new ItemMessageDto("item2", "description Item2", false, null);
        itemResponseDto1.setId(1L);
        itemResponseDto2.setId(2L);
        itemMessageDto1.setId(1L);
        itemMessageDto2.setId(2L);
        user1 = new User("NameTest", "test@test.ru");
        user1.setId(1L);
        user2 = new User("NameTest2", "test@test2.ru");
        user2.setId(2L);
        userService.save(user1);
        userService.save(user2);
        item1 = new Item("item1", "description Item1", true, user1, null);
        item2 = new Item("item2", "description Item2", false, user1, null);
        item1.setId(1L);
        item2.setId(2L);
        itemService.save(itemMessageDto1, 1L);
        itemService.save(itemMessageDto2, 1L);
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
        Booking bookingTest = bookingService.save(booking1, 2L, 1L);
        Assertions.assertEquals(bookingTest, booking1);
    }

    @Test
    void patch() {
        bookingService.save(booking1, 2L, 1L);
        Booking bookingTest = bookingService.patch(true, 1L, 1L);
        Assertions.assertEquals(bookingTest.getId(), booking1.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(bookingTest.getId(), booking1.getId()),
                () -> Assertions.assertEquals(bookingTest.getItem(), booking1.getItem()),
                () -> Assertions.assertEquals(bookingTest.getBooker(), booking1.getBooker())
        );
    }

    @Test
    void get() {
        bookingService.save(booking1, 2L, 1L);
        Booking bookingTest = bookingService.get(1L, 1L);
        Assertions.assertAll(
                () -> Assertions.assertEquals(bookingTest.getId(), booking1.getId()),
                () -> Assertions.assertEquals(bookingTest.getItem(), booking1.getItem()),
                () -> Assertions.assertEquals(bookingTest.getBooker(), booking1.getBooker())
        );
    }

    @Test
    void getAllBooker() {
        bookingService.save(booking1, 2L, 1L);
        item2.setAvailable(true);
        itemService.patch(item2, 2L, 1L);
        bookingService.save(booking2, 2L, 2L);
        List<Booking> bookingTest = bookingService.getAllBooker(2L, State.ALL, 0, 5);
        Assertions.assertAll(
                () -> Assertions.assertEquals(bookingTest.get(0).getId(), booking1.getId()),
                () -> Assertions.assertEquals(bookingTest.get(1).getItem(), booking2.getItem()),
                () -> Assertions.assertEquals(bookingTest.get(0).getBooker(), booking1.getBooker())
        );
    }

    @Test
    void getAllOwner() {
        bookingService.save(booking1, 2L, 1L);
        item2.setAvailable(true);
        itemService.patch(item2, 2L, 1L);
        bookingService.save(booking2, 2L, 2L);
        List<Booking> bookingTest = bookingService.getAllOwner(1L, State.ALL, 0, 5);
        Assertions.assertAll(
                () -> Assertions.assertEquals(bookingTest.get(0).getId(), booking1.getId()),
                () -> Assertions.assertEquals(bookingTest.get(1).getItem(), booking2.getItem()),
                () -> Assertions.assertEquals(bookingTest.get(0).getBooker(), booking1.getBooker())
        );
    }
}
