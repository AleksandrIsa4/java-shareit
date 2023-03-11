package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTestIntegration {

    private final UserService userService;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void getTest() {
        userService.save(user1);
        User userTest = userService.get(1L);
        Assertions.assertEquals(user1, userTest);
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
