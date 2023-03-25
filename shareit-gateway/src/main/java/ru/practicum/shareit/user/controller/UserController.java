package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserMessageDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody UserMessageDto dto) {
        log.info("Post User with dto {}", dto);
        return userClient.postUser(dto);
    }

    @PatchMapping(value = "/{userId}")
    public ResponseEntity<Object> patch(@RequestBody UserMessageDto dto, @PathVariable @NotNull long userId) {
        log.info("Patch User with dto {}, userId={}", dto, userId);
        return userClient.patchUser(dto, userId);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Object> get(@PathVariable @NotNull long userId) {
        log.info("Get User with userId={}", userId);
        return userClient.getUser(userId);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable @NotNull long userId) {
        log.info("Delete User with userId={}", userId);
        userClient.deleteUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Get all User");
        return userClient.getUsers();
    }
}
