package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserMessageDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto save(@RequestBody UserMessageDto dto) {
        log.info("Post User with dto {}", dto);
        User user = UserMapper.toEntity(dto);
        user = userService.save(user);
        return UserMapper.toDto(user);
    }

    @PatchMapping(value = "/{userId}")
    public UserResponseDto patch(@RequestBody UserMessageDto dto, @PathVariable Long userId) {
        log.info("Patch User with dto {}, userId={}", dto, userId);
        User user = UserMapper.toEntity(dto);
        user = userService.patch(user, userId);
        return UserMapper.toDto(user);
    }

    @GetMapping(value = "/{userId}")
    public UserResponseDto get(@PathVariable Long userId) {
        log.info("Get User with userId={}", userId);
        User user = userService.get(userId);
        return UserMapper.toDto(user);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Delete User with userId={}", userId);
        userService.delete(userId);
    }

    @GetMapping
    public List<UserResponseDto> getAll() {
        log.info("Get all User");
        return userService.getAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }
}
