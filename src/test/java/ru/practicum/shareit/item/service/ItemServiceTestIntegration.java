package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTestIntegration {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    ItemResponseDto itemResponseDto1;
    ItemMessageDto itemMessageDto1;
    ItemResponseDto itemResponseDto2;
    ItemMessageDto itemMessageDto2;
    Item item1;
    Item item2;
    User user;

    @BeforeEach
    void setUp() {
        itemResponseDto1 = new ItemResponseDto("item1", "description Item1", true, null, null, null, null);
        itemMessageDto1 = new ItemMessageDto("item1", "description Item1", true, null);
        itemResponseDto2 = new ItemResponseDto("item2", "description Item2", false, null, null, null, null);
        itemMessageDto2 = new ItemMessageDto("item2", "description Item2", false, null);
        user = new User("NameTest", "test@test.ru");
        user.setId(1L);
        item1 = new Item("item1", "description Item1", true, user, null);
        item2 = new Item("item2", "description Item2", false, user, null);
        itemResponseDto1.setId(1L);
        itemResponseDto2.setId(2L);
        itemMessageDto1.setId(1L);
        itemMessageDto2.setId(2L);
        item1.setId(1L);
        item2.setId(2L);
        userService.save(user);
    }

    @Test
    void saveTest() {
        ItemResponseDto itemResponseDto1Test = itemService.save(itemMessageDto1, 1L);
        Assertions.assertEquals(itemResponseDto1Test, itemResponseDto1);
    }

    @Test
    void patchTest() {
        itemService.save(itemMessageDto1, 1L);
        Item itemTest = itemService.patch(item2, 1L, 1L);
        Assertions.assertEquals(itemTest, item2);
    }

    @Test
    void patchTestWrong() {
        itemService.save(itemMessageDto1, 1L);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.patch(item2, 1L, 2L);
        });
    }

    @Test
    void getTest() {
        itemService.save(itemMessageDto1, 1L);
        itemService.save(itemMessageDto2, 1L);
        ItemResponseDto itemResponseDtoTest = itemService.get(2L, 1L);
        itemResponseDto2.setComments(new ArrayList<>());
        Assertions.assertEquals(itemResponseDtoTest, itemResponseDto2);
    }

    @Test
    void deleteTest() {
        itemService.save(itemMessageDto1, 1L);
        itemService.save(itemMessageDto2, 1L);
        itemService.delete(1L, 1L);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemService.get(1L, 1L);
        });
    }

    @Test
    void getAllTest() {
        itemService.save(itemMessageDto1, 1L);
        itemService.save(itemMessageDto2, 1L);
        List<ItemResponseDto> itemResponseDtoTest = itemService.getAll(1L, 0, 5);
        itemResponseDto1.setComments(new ArrayList<>());
        itemResponseDto2.setComments(new ArrayList<>());
        List<ItemResponseDto> itemResponseDtos = List.of(itemResponseDto1, itemResponseDto2);
        Assertions.assertEquals(itemResponseDtos, itemResponseDtoTest);
    }

    @Test
    void searchTest() {
        itemService.save(itemMessageDto1, 1L);
        itemService.save(itemMessageDto2, 1L);
        List<Item> itemTest = itemService.search("item1", 0, 5);
        List<Item> items = List.of(item1);
        Assertions.assertEquals(itemTest, items);
    }

    @Test
    void saveCommentTest() throws InterruptedException {
        itemService.save(itemMessageDto1, 1L);
        User user2 = new User("NameTest2", "test@test2.ru");
        user2.setId(2L);
        userService.save(user2);
        Booking booking = new Booking(item1, user2, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), Status.APPROVED);
        booking.setId(1L);
        bookingService.save(booking, 2L, 1L);
        bookingService.patch(true, 1L, 1L);
        Thread.sleep(2000);
        Comment comment = new Comment("test comment", item1, user, LocalDateTime.now());
        Comment commentTest = itemService.saveComment(comment, 2L, 1L);
        Assertions.assertEquals(commentTest, comment);
    }
}
