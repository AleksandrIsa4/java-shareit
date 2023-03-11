package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentMessageDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    @MockBean
    ItemService itemService;

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
    }


    @Test
    void saveTest() throws Exception {
        when(itemService.save(Mockito.any(ItemMessageDto.class), Mockito.anyLong()))
                .thenReturn(itemResponseDto1);
        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemMessageDto1))
                        .header(HEADER_REQUEST, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemResponseDto1.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemResponseDto1.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemResponseDto1.getAvailable()), Boolean.class));
        Mockito.verify(itemService, Mockito.times(1))
                .save(Mockito.any(ItemMessageDto.class), Mockito.anyLong());
    }

    @Test
    void patchTest() throws Exception {
        when(itemService.patch(Mockito.any(Item.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(item2);
        mockMvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemMessageDto2))
                        .header(HEADER_REQUEST, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemResponseDto2.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemResponseDto2.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemResponseDto2.getAvailable()), Boolean.class));
        Mockito.verify(itemService, Mockito.times(1))
                .patch(Mockito.any(Item.class), Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void getItemTest() throws Exception {
        Mockito.when(itemService.get(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemResponseDto1);
        mockMvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemResponseDto1.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemResponseDto1.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemResponseDto1.getAvailable()), Boolean.class));
        Mockito.verify(itemService, Mockito.times(1))
                .get(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void getAllTest() throws Exception {
        List<ItemResponseDto> itemsDto = List.of(itemResponseDto1, itemResponseDto2);
        Mockito.when(itemService.getAll(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(itemsDto);
        mockMvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[1].name", Matchers.is(itemResponseDto2.getName()), String.class))
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
        Mockito.verify(itemService, Mockito.times(1))
                .getAll(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void searchTest() throws Exception {
        List<Item> items = List.of(item1, item2);
        when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(items);
        mockMvc.perform(get("/items/search?text='item'")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
        Mockito.verify(itemService, Mockito.times(1))
                .search(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    void saveCommentTest() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        Comment comment = new Comment("comment1", item1, user, time);
        CommentMessageDto commentMessageDto = new CommentMessageDto("comment1");
        CommentResponseDto commentResponseDto = new CommentResponseDto("comment1", "NameTest", time);
        when(itemService.saveComment(Mockito.any(Comment.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(comment);
        mockMvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentMessageDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentResponseDto.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(commentResponseDto.getAuthorName()), String.class));
    }
}