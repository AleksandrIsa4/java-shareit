package ru.practicum.shareit.request.service;

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
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class RequestServiceTest {

    @Mock
    UserService userService;
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    RequestService requestService;
    ItemResponseDto itemResponseDto1;
    ItemResponseDto itemResponseDto2;
    Item item1;
    Item item2;
    User user;
    ItemRequestMessageDto itemRequestMessageDto2;
    ItemRequestResponceDto itemRequestResponceDto1;
    ItemRequestResponceDto itemRequestResponceDto2;
    ItemRequest itemRequest1;
    ItemRequest itemRequest2;

    @BeforeEach
    void init() {
        requestService = new RequestService(userService,
                itemRequestRepository,
                itemRepository);
        itemResponseDto1 = new ItemResponseDto("item1", "description Item1", true, null, null, null, null);
        itemResponseDto2 = new ItemResponseDto("item2", "description Item2", false, null, null, null, null);
        user = new User("NameTest", "test@test.ru");
        user.setId(1L);
        item1 = new Item("item1", "description Item1", true, user, null);
        item2 = new Item("item2", "description Item2", false, user, null);
        itemRequestMessageDto2 = new ItemRequestMessageDto("description  ItemRequest2");
        LocalDateTime now = LocalDateTime.now();
        itemRequestResponceDto1 = new ItemRequestResponceDto("description  ItemRequest1", now, List.of(itemResponseDto1));
        itemRequestResponceDto2 = new ItemRequestResponceDto("description  ItemRequest2", now, null);
        itemRequest1 = new ItemRequest("description  ItemRequest1", user, now);
        itemRequest2 = new ItemRequest("description  ItemRequest2", user, now);
    }


    @Test
    void save() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(itemRequest2);
        ItemRequestResponceDto itemRequestResponceDtoTest = requestService.save(itemRequestMessageDto2, 1L);
        Assertions.assertEquals(itemRequestResponceDtoTest, itemRequestResponceDto2);
    }

    @Test
    void getAllMy() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        List<ItemRequest> itemRequests = List.of(itemRequest1);
        Mockito.when(itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(Mockito.anyLong()))
                .thenReturn(itemRequests);
        List<Item> items = List.of(item1);
        Mockito.when(itemRepository.findAllByRequestId(Mockito.any()))
                .thenReturn(items);
        List<ItemRequestResponceDto> itemRequestResponceDtoTest = requestService.getAllMy(1L);
        List<ItemRequestResponceDto> dto = List.of(itemRequestResponceDto1);
        Assertions.assertEquals(itemRequestResponceDtoTest, dto);
    }

    @Test
    void getRequest() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest2));
        List<Item> items = List.of(item2);
        Mockito.when(itemRepository.findAllByRequestId(Mockito.any()))
                .thenReturn(items);
        ItemRequestResponceDto itemRequestResponceDtoTest = requestService.getRequest(1L, 2L);
        itemRequestResponceDto2.setItems(List.of(itemResponseDto2));
        Assertions.assertEquals(itemRequestResponceDtoTest, itemRequestResponceDto2);
    }

    @Test
    void getRequestWrong() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(5L);
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> requestService.getRequest(5L, 1L));
        Assertions.assertEquals("404 NOT_FOUND \"указанного запроса нет\"", exception.getMessage());
    }

    @Test
    void getAllPagination() {
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        List<ItemRequest> itemRequests = List.of(itemRequest1);
        Mockito.when(itemRequestRepository.findAllRequest(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(itemRequests);
        List<Item> items = List.of(item1);
        Mockito.when(itemRepository.findAllByRequestId(Mockito.any()))
                .thenReturn(items);
        List<ItemRequestResponceDto> itemRequestResponceDtoTest = requestService.getAllPagination(1L, 0, 2);
        List<ItemRequestResponceDto> dto = List.of(itemRequestResponceDto1);
        Assertions.assertEquals(itemRequestResponceDtoTest, dto);
    }
}