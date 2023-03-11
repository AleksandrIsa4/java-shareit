package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    ItemService itemService;
    @Mock
    UserService userService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;
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

    @BeforeEach
    void setUp() {
        itemService = new ItemService(mockItemRepository,
                userService,
                bookingRepository,
                userRepository,
                commentRepository,
                itemRequestRepository);
        itemResponseDto1 = new ItemResponseDto("item1", "description Item1", true, null, null, null, null);
        itemMessageDto1 = new ItemMessageDto("item1", "description Item1", true, null);
        itemResponseDto2 = new ItemResponseDto("item2", "description Item2", false, null, null, null, null);
        itemMessageDto2 = new ItemMessageDto("item2", "description Item2", false, null);
        user = new User("NameTest", "test@test.ru");
        user.setId(1L);
        item1 = new Item("item1", "description Item1", true, user, null);
        item2 = new Item("item2", "description Item2", false, user, null);
    }

    @Test
    void save() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item1);
        ItemResponseDto itemResponseDto3 = itemService.save(itemMessageDto1, 1L);
        Assertions.assertEquals(itemResponseDto1, itemResponseDto3);
    }

    @Test
    void patch() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        Mockito.when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item2);
        Item item3 = itemService.patch(item1, 1L, 2L);
        Assertions.assertEquals(item2, item3);
    }

    @Test
    void get() {
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        ItemResponseDto itemResponseDto3 = itemService.get(1L, 2L);
        itemResponseDto1.setComments(new ArrayList<>());
        Assertions.assertEquals(itemResponseDto1, itemResponseDto3);
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
}