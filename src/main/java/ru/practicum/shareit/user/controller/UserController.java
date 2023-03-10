package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserMessageDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto save(@Valid @RequestBody UserMessageDto dto) {
        User user = UserMapper.toEntity(dto);
        user = userService.save(user);
        return UserMapper.toDto(user);
    }

    @PatchMapping(value = "/{userId}")
    public UserResponseDto patch(@RequestBody UserMessageDto dto, @PathVariable() @NotNull Long userId) {
        User user = UserMapper.toEntity(dto);
        user = userService.patch(user, userId);
        return UserMapper.toDto(user);
    }

    @GetMapping(value = "/{userId}")
    public UserResponseDto get(@PathVariable() @NotNull Long userId) {
        User user = userService.get(userId);
        return UserMapper.toDto(user);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable() @NotNull Long userId) {
        userService.delete(userId);
    }

    @GetMapping
    public List<UserResponseDto> getAll() {
        return userService.getAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }
}
