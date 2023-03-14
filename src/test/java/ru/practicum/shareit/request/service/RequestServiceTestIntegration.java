package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestServiceTestIntegration {

    final ItemService itemService;
    final UserService userService;
    final RequestService requestService;
    ItemResponseDto itemResponseDto1;
    ItemMessageDto itemMessageDto1;
    ItemMessageDto itemMessageDto2;
    Item item1;
    Item item2;
    User user;
    ItemRequestMessageDto itemRequestMessageDto2;
    ItemRequestResponceDto itemRequestResponceDto2;
    ItemRequest itemRequest1;
    ItemRequest itemRequest2;

    @BeforeEach
    void init() {
        itemResponseDto1 = new ItemResponseDto("item1", "description Item1", true, null, null, null, null);
        itemMessageDto1 = new ItemMessageDto("item1", "description Item1", true, null);
        itemMessageDto2 = new ItemMessageDto("item2", "description Item2", false, null);
        user = new User("NameTest", "test@test.ru");
        user.setId(1L);
        userService.save(user);
        item1 = new Item("item1", "description Item1", true, user, null);
        item2 = new Item("item2", "description Item2", false, user, null);
        itemRequestMessageDto2 = new ItemRequestMessageDto("description  ItemRequest2");
        LocalDateTime now = LocalDateTime.now();
        itemService.save(itemMessageDto1, 1L);
        itemService.save(itemMessageDto2, 1L);
        itemRequestResponceDto2 = new ItemRequestResponceDto("description  ItemRequest2", now, null);
        itemRequest1 = new ItemRequest("description  ItemRequest1", user, now);
        itemRequest2 = new ItemRequest("description  ItemRequest2", user, now);
    }

    @Test
    void save() {
        ItemRequestResponceDto itemRequestResponceDtoTest = requestService.save(itemRequestMessageDto2, 1L);
        Assertions.assertEquals(itemRequestResponceDto2.getDescription(), itemRequestResponceDtoTest.getDescription());
    }

    @Test
    void getAllMy() {
        requestService.save(itemRequestMessageDto2, 1L);
        List<ItemRequestResponceDto> itemRequestResponceDtoTest = requestService.getAllMy(1L);
        Assertions.assertEquals(itemRequestResponceDto2.getDescription(), itemRequestResponceDtoTest.get(0).getDescription());
    }

    @Test
    void getAllPagination() {
        User user2 = new User("NameTest2", "test@test2.ru");
        user2.setId(2L);
        userService.save(user2);
        requestService.save(itemRequestMessageDto2, 2L);
        List<ItemRequestResponceDto> itemRequestResponceDtoTest = requestService.getAllPagination(1L, 0, 5);
        Assertions.assertEquals(itemRequestResponceDto2.getDescription(), itemRequestResponceDtoTest.get(0).getDescription());
    }
}
