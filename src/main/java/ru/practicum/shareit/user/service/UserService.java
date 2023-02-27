package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.abstraction.PatchMap;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository storage;
    private final PatchMap<User> patchMap = new PatchMap<>();

    public User save(User user) {
        return storage.save(user);
    }

    @Transactional
    public User patch(User user, Long id) {
        User userFind = storage.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "указанного пользователя нет"));
        user = patchMap.patchObject(user, userFind);
        return storage.save(user);
    }

    public User get(Long id) {
        return storage.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "указанного пользователя нет"));
    }

    public void delete(Long id) {
        storage.deleteById(id);
    }

    public List<User> getAll() {
        return storage.findAll();
    }
}
