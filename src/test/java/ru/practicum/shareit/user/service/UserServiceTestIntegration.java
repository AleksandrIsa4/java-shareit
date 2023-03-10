package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceTestIntegration {

    final UserService userService;
    User user1;
    User user2;

    @BeforeEach
    void init() {
        user1 = new User("NameTest", "test@test.ru");
        user1.setId(1L);
        user2 = new User("NameTest2", "test@test.ru2");
        user2.setId(2L);
    }

    @Test
    void saveTest() {
        User userTest = userService.save(user1);
        Assertions.assertEquals(user1, userTest);
    }

    @Test
    void patchTest() {
        userService.save(user1);
        User userTest = userService.patch(user2, 1L);
        Assertions.assertEquals(user2, userTest);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.patch(user2, 5L);
        });
    }

    @Test
    void getTest() {
        userService.save(user1);
        User userTest = userService.get(1L);
        user1.equals(user2);
        Assertions.assertAll(
                () -> Assertions.assertEquals(user1, userTest),
                () -> Assertions.assertEquals(userTest.getId(), userTest.getId()),
                () -> Assertions.assertThrows(RuntimeException.class, () -> userService.get(5L))
        );
    }

    @Test
    void getTestWrong() {
        Assertions.assertThrows(RuntimeException.class, () -> userService.get(5L));
    }

    @Test
    void getAllTest() {
        userService.save(user1);
        userService.save(user2);
        List<User> users = List.of(user1, user2);
        List<User> usersTest = userService.getAll();
        Assertions.assertEquals(usersTest, users);
    }

    @Test
    void deleteTest() {
        userService.save(user1);
        userService.save(user2);
        List<User> users = List.of(user1, user2);
        List<User> usersTest = userService.getAll();
        Assertions.assertEquals(usersTest, users);
        userService.delete(1L);
        usersTest = userService.getAll();
        Assertions.assertNotEquals(usersTest, users);
    }
}
