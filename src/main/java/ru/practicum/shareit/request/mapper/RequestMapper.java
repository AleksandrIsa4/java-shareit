package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestMapper {

    public static ItemRequestResponceDto toDto(ItemRequest itemRequest) {
        ItemRequestResponceDto itemRequestResponceDto = new ItemRequestResponceDto();
        itemRequestResponceDto.setId(itemRequest.getId());
        itemRequestResponceDto.setDescription(itemRequest.getDescription());
        itemRequestResponceDto.setCreated(itemRequest.getCreated());
        return itemRequestResponceDto;
    }

    public static ItemRequest toEntity(ItemRequestMessageDto dto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setId(dto.getId());
        itemRequest.setDescription(dto.getDescription());
        return itemRequest;
    }
}
