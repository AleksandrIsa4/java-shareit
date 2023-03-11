package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserMessageDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void saveTest() throws Exception {
        UserMessageDto userMessageDto = new UserMessageDto("NameTest", "test@test.ru");
        userMessageDto.setId(1L);
        UserResponseDto userResponseDto = new UserResponseDto("NameTest", "test@test.ru");
        userResponseDto.setId(1L);
        User user = new User("NameTest", "test@test.ru");
        user.setId(1L);
        Mockito.when(userService.save(Mockito.any(User.class)))
                .thenReturn(user);
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userMessageDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(userResponseDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseDto.getEmail()), String.class));
        Mockito.verify(userService, Mockito.times(1))
                .save(Mockito.any(User.class));
    }

    @Test
    void patchTest() throws Exception {
        UserMessageDto userMessageDto = new UserMessageDto("NameTest2", "test@test.ru2");
        userMessageDto.setId(1L);
        UserResponseDto userResponseDto = new UserResponseDto("NameTest2", "test@test.ru2");
        userResponseDto.setId(1L);
        User user = new User("NameTest2", "test@test.ru2");
        user.setId(1L);
        Mockito.when(userService.patch(Mockito.any(User.class), Mockito.anyLong()))
                .thenReturn(user);
        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userMessageDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(userResponseDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseDto.getEmail()), String.class));
        Mockito.verify(userService, Mockito.times(1))
                .patch(Mockito.any(User.class), Mockito.anyLong());
    }

    @Test
    void getTest() throws Exception {
        UserMessageDto userMessageDto = new UserMessageDto("NameTest", "test@test.ru");
        userMessageDto.setId(1L);
        UserResponseDto userResponseDto = new UserResponseDto("NameTest", "test@test.ru");
        userResponseDto.setId(1L);
        User user = new User("NameTest", "test@test.ru");
        user.setId(1L);
        Mockito.when(userService.get(Mockito.anyLong()))
                .thenReturn(user);
        mockMvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(userResponseDto.getName()), String.class))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseDto.getEmail()), String.class));
        Mockito.verify(userService, Mockito.times(1))
                .get(Mockito.anyLong());
    }

    @Test
    void getAllTest() throws Exception {
        UserMessageDto userMessageDto = new UserMessageDto("NameTest", "test@test.ru");
        userMessageDto.setId(1L);
        UserMessageDto userMessageDto2 = new UserMessageDto("NameTest2", "test@test.ru2");
        userMessageDto.setId(2L);
        UserResponseDto userResponseDto = new UserResponseDto("NameTest", "test@test.ru");
        userResponseDto.setId(1L);
        UserResponseDto userResponseDto2 = new UserResponseDto("NameTest2", "test@test.ru2");
        userResponseDto.setId(2L);
        User user = new User("NameTest", "test@test.ru");
        user.setId(1L);
        User user2 = new User("NameTest2", "test@test.ru2");
        user.setId(2L);
        List<User> users = List.of(user, user2);
        Mockito.when(userService.getAll())
                .thenReturn(users);
        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[1].name", Matchers.is(userResponseDto2.getName()), String.class))
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
        Mockito.verify(userService, Mockito.times(1))
                .getAll();
    }

    @Test
    void deleteTest() throws Exception {
        Mockito.doNothing().when(userService).delete(Mockito.anyLong());
        mockMvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1))
                .delete(Mockito.anyLong());
    }
}