package ru.practicum.shareit.user.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.abstraction.PatchMap;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage<User, Long> {

    private long generator = 0;
    private final Map<Long, User> users = new HashMap<>();
    private final PatchMap<User> patchMap = new PatchMap<>();

    @Override
    public User save(User user) {
        checkEmail(user.getEmail());
        user.setId(++generator);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User update(User user, Long id) {
        checkEmail(user.getEmail());
        user = patchMap.patchObject(user, get(id));
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(Long id) {
        checkUserId(id);
        return users.get(id);
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public void checkEmail(String email) {
        for (User memoryUser : users.values()) {
            if (memoryUser.getEmail().equals(email)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "указанный email уже есть");
            }
        }
    }

    public void checkUserId(Long id) {
        if (!(users.containsKey(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "указанного пользователя нет");
        }
    }
}
