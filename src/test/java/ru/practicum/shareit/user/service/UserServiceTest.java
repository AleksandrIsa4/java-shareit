package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserServiceTest {

    UserService userService;
    @Mock
    UserRepository mockUserRepository;
    private User user;

    @BeforeEach
    void init() {
        userService = new UserService(mockUserRepository);
        user = new User("NameTest", "test@test.ru");
        user.setId(1L);
    }

    @Test
    void saveTest() {
        Mockito.when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
        User user2 = userService.save(user);
        Assertions.assertEquals(user, user2);
    }

    @Test
    void patchTest() {
        User user2 = new User("NameTest2", "test@test.ru2");
        user2.setId(1L);
        Mockito.when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(user2);
        User user3 = userService.patch(user, user.getId());
        Assertions.assertEquals(user3, user2);
        Mockito.when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> userService.patch(user, user.getId()));
        Assertions.assertEquals("404 NOT_FOUND \"указанного пользователя нет\"", exception.getMessage());
    }

    @Test
    void getTest() {
        Mockito.when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        User user2 = userService.get(user.getId());
        Assertions.assertEquals(user, user2);
        Mockito.when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> userService.get(1L));
        Assertions.assertEquals("404 NOT_FOUND \"указанного пользователя нет\"", exception.getMessage());
    }

    @Test
    void deleteTest() {
        Mockito.doNothing().when(mockUserRepository).deleteById(Mockito.anyLong());
        userService.delete(1L);
    }

    @Test
    void getAllTest() {
        List<User> users = new ArrayList<>();
        User user2 = new User("NameTest2", "test@test.ru2");
        user2.setId(2L);
        users.add(user);
        users.add(user2);
        Mockito.when(mockUserRepository.findAll())
                .thenReturn(users);
        List<User> users2 = userService.getAll();
        Assertions.assertAll(
                () -> Assertions.assertNotNull(users2),
                () -> Assertions.assertEquals(users2.get(1), user2),
                () -> Assertions.assertEquals(users2.get(0), user)
        );
    }
}