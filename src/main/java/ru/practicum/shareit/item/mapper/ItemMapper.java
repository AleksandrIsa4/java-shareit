package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

public final class ItemMapper {

    private ItemMapper() {
    }

    public static ItemResponseDto toDto(Item item) {
        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.getAvailable());
        return itemResponseDto;
    }

    public static Item toEntity(ItemMessageDto dto, Long itemId) {
        Item item = new Item();
        if (itemId == null) {
            item.setId(dto.getId());
        } else {
            item.setId(itemId);
        }
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }
}
