package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserStorage<User, Long> storage;

    public User save(User user) {
        return storage.save(user);
    }

    public User patch(User user, Long id) {
        return storage.update(user, id);
    }

    public User get(Long id) {
        return storage.get(id);
    }

    public void delete(Long id) {
        storage.delete(id);
    }

    public List<User> getAll() {
        return storage.getAll();
    }
}
