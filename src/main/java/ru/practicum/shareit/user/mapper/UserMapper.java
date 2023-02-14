package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserMessageDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
    }

    public static User toEntity(UserMessageDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }
}
