package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    @MockBean
    RequestService requestService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
    ItemResponseDto itemResponseDto1;
    ItemMessageDto itemMessageDto1;
    ItemResponseDto itemResponseDto2;
    ItemMessageDto itemMessageDto2;
    Item item1;
    Item item2;
    User user;
    ItemRequestMessageDto itemRequestMessageDto1;
    ItemRequestMessageDto itemRequestMessageDto2;
    ItemRequestResponceDto itemRequestResponceDto1;
    ItemRequestResponceDto itemRequestResponceDto2;
    ItemRequest itemRequest1;
    ItemRequest itemRequest2;

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
        itemRequestMessageDto1 = new ItemRequestMessageDto("description  ItemRequest1");
        itemRequestMessageDto2 = new ItemRequestMessageDto("description  ItemRequest2");
        LocalDateTime now = LocalDateTime.now();
        itemRequestResponceDto1 = new ItemRequestResponceDto("description  ItemRequest1", now, List.of(itemResponseDto1));
        itemRequestResponceDto2 = new ItemRequestResponceDto("description  ItemRequest2", now, List.of(itemResponseDto2));
        itemRequest1 = new ItemRequest("description  ItemRequest1", user, now);
        itemRequest2 = new ItemRequest("description  ItemRequest2", user, now);
    }

    @Test
    void save() throws Exception {
        when(requestService.save(Mockito.any(ItemRequestMessageDto.class), Mockito.anyLong()))
                .thenReturn(itemRequestResponceDto1);
        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestMessageDto1))
                        .header(HEADER_REQUEST, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestResponceDto1.getDescription()), String.class));
        Mockito.verify(requestService, Mockito.times(1))
                .save(Mockito.any(ItemRequestMessageDto.class), Mockito.anyLong());
    }

    @Test
    void getAllMy() throws Exception {
        List<ItemRequestResponceDto> itemRequestResponceDtos = List.of(itemRequestResponceDto1, itemRequestResponceDto2);
        when(requestService.getAllMy(Mockito.anyLong()))
                .thenReturn(itemRequestResponceDtos);
        mockMvc.perform(get("/requests")
                        .header(HEADER_REQUEST, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].description", is(itemRequestResponceDto1.getDescription()), String.class))
                .andExpect(jsonPath("[1].description", is(itemRequestResponceDto2.getDescription()), String.class));
        Mockito.verify(requestService, Mockito.times(1))
                .getAllMy(Mockito.anyLong());
    }

    @Test
    void getRequest() throws Exception {
        when(requestService.getRequest(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemRequestResponceDto1);
        mockMvc.perform(get("/requests/1")
                        .header(HEADER_REQUEST, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestResponceDto1.getDescription()), String.class));
        Mockito.verify(requestService, Mockito.times(1))
                .getRequest(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void testGetAll() throws Exception {
        List<ItemRequestResponceDto> itemRequestResponceDtos = List.of(itemRequestResponceDto1, itemRequestResponceDto2);
        when(requestService.getAllPagination(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(itemRequestResponceDtos);
        mockMvc.perform(get("/requests/all")
                        .header(HEADER_REQUEST, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].description", is(itemRequestResponceDto1.getDescription()), String.class))
                .andExpect(jsonPath("[1].description", is(itemRequestResponceDto2.getDescription()), String.class));
        Mockito.verify(requestService, Mockito.times(1))
                .getAllPagination(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }
}