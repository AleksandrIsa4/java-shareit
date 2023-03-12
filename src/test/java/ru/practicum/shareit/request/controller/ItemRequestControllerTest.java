package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemRequestControllerTest {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";
    @MockBean
    RequestService requestService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    ItemResponseDto itemResponseDto1;
    ItemResponseDto itemResponseDto2;
    User user;
    ItemRequestMessageDto itemRequestMessageDto1;
    ItemRequestResponceDto itemRequestResponceDto1;
    ItemRequestResponceDto itemRequestResponceDto2;

    @BeforeEach
    void init() {
        itemResponseDto1 = new ItemResponseDto("item1", "description Item1", true, null, null, null, null);
        itemResponseDto2 = new ItemResponseDto("item2", "description Item2", false, null, null, null, null);
        user = new User("NameTest", "test@test.ru");
        user.setId(1L);
        itemRequestMessageDto1 = new ItemRequestMessageDto("description  ItemRequest1");
        LocalDateTime now = LocalDateTime.now();
        itemRequestResponceDto1 = new ItemRequestResponceDto("description  ItemRequest1", now, List.of(itemResponseDto1));
        itemRequestResponceDto2 = new ItemRequestResponceDto("description  ItemRequest2", now, List.of(itemResponseDto2));
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