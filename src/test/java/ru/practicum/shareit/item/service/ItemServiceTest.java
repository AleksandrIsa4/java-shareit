package ru.practicum.shareit.item.service;

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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemServiceTest {

    @Mock
    ItemService itemService;
    @Mock
    UserService userService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    ItemRepository mockItemRepository;
    ItemResponseDto itemResponseDto1;
    ItemMessageDto itemMessageDto1;
    ItemResponseDto itemResponseDto2;
    ItemMessageDto itemMessageDto2;
    Item item1;
    Item item2;
    User user;
    ItemRequest itemRequest1;

    @BeforeEach
    void init() {
        itemService = new ItemService(mockItemRepository,
                userService,
                bookingRepository,
                commentRepository,
                itemRequestRepository);
        itemResponseDto1 = new ItemResponseDto("item1", "description Item1", true, null, null, null, 1L);
        itemMessageDto1 = new ItemMessageDto("item1", "description Item1", true, 1L);
        itemResponseDto2 = new ItemResponseDto("item2", "description Item2", false, null, null, null, null);
        itemMessageDto2 = new ItemMessageDto("item2", "description Item2", false, null);
        user = new User("NameTest", "test@test.ru");
        user.setId(1L);
        itemRequest1 = new ItemRequest("description  ItemRequest1", user, LocalDateTime.now());
        itemRequest1.setId(1L);
        item1 = new Item("item1", "description Item1", true, user, itemRequest1);
        item2 = new Item("item2", "description Item2", false, user, null);
    }

    @Test
    void save() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item1);
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest1));
        ItemResponseDto itemResponseDto3 = itemService.save(itemMessageDto1, 1L);
        Assertions.assertEquals(itemResponseDto1, itemResponseDto3);
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> itemService.save(itemMessageDto1, 1L));
        Assertions.assertEquals("404 NOT_FOUND \"указанного запроса нет\"", exception.getMessage());
    }

    @Test
    void patch() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        Mockito.when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item2);
        Item item3 = itemService.patch(item1, 1L, 1L);
        Assertions.assertEquals(item2, item3);
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> itemService.patch(item1, 1L, 2L));
        Assertions.assertEquals("404 NOT_FOUND \"указанного предмета нет\"", exception.getMessage());
    }

    @Test
    void patchWrong() {
        User user2 = new User("NameTest2", "test@test.ru2");
        user2.setId(2L);
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user2);
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> itemService.patch(item1, 1L, 3L));
        Assertions.assertEquals("404 NOT_FOUND \"другой пользователь\"", exception.getMessage());
    }

    @Test
    void get() {
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        ItemResponseDto itemResponseDto3 = itemService.get(1L, 2L);
        itemResponseDto1.setComments(new ArrayList<>());
        Assertions.assertEquals(itemResponseDto1, itemResponseDto3);
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> itemService.get(1L, 2L));
        Assertions.assertEquals("404 NOT_FOUND \"указанного предмета нет\"", exception.getMessage());
    }

    @Test
    void getAll() {
        item1.setId(1L);
        item2.setId(2L);
        List<Item> items = List.of(item1, item2);
        Mockito.when(mockItemRepository.findAllByOwnerId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(items);
        List<ItemResponseDto> itemResponseDto3 = itemService.getAll(1L, 0, 5);
        itemResponseDto1.setComments(new ArrayList<>());
        itemResponseDto2.setComments(new ArrayList<>());
        Assertions.assertAll(
                () -> Assertions.assertNotNull(itemResponseDto3),
                () -> Assertions.assertEquals(itemResponseDto3.get(0), itemResponseDto1),
                () -> Assertions.assertEquals(itemResponseDto3.get(1), itemResponseDto2)
        );
    }

    @Test
    void search() {
        Mockito.when(mockItemRepository.search(Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(List.of(item2));
        List<Item> items = itemService.search("Item2", 0, 5);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(items),
                () -> Assertions.assertEquals(items, List.of(item2))
        );
    }

    @Test
    void deleteTest() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.doNothing().when(mockItemRepository).deleteById(Mockito.anyLong());
        itemService.delete(1L, 1L);
    }

    @Test
    void saveComment() {
        Comment comment = new Comment("test comment", item1, user, LocalDateTime.now());
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        Booking booking = new Booking(item1, user, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), Status.APPROVED);
        booking.setId(1L);
        Mockito.when(bookingRepository.findTop1ByBookerIdAndItemIdAndEndIsBefore(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(booking);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);
        Comment commentTest = itemService.saveComment(comment, 1L, 1L);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(commentTest),
                () -> Assertions.assertEquals(comment.getText(), commentTest.getText()),
                () -> Assertions.assertEquals(comment.getItem(), commentTest.getItem())
        );
    }

    @Test
    void saveCommentWrong() {
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Comment comment = new Comment("test comment", item1, user, LocalDateTime.now());
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> itemService.saveComment(comment, 1L, 1L));
        Assertions.assertEquals("404 NOT_FOUND \"указанного предмета нет\"", exception.getMessage());
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        Mockito.when(bookingRepository.findTop1ByBookerIdAndItemIdAndEndIsBefore(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(null);
        Exception exception2 = Assertions.assertThrows(RuntimeException.class,
                () -> itemService.saveComment(comment, 1L, 1L));
        Assertions.assertEquals("400 BAD_REQUEST \"брони пользователем не было\"", exception2.getMessage());
    }
}