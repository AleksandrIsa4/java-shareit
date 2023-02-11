package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

@Component
public class ItemMapper {

    private static UserService userService;

    public ItemMapper(UserService userService) {
        this.userService = userService;
    }

    public static ItemResponseDto toDto(Item item) {
        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.isAvailable());
        return itemResponseDto;
    }

    public static Item toEntity(ItemMessageDto dto, Long idUser, Long itemId) {
        Item item = new Item();
        if (itemId == null) {
            item.setId(dto.getId());
        } else {
            item.setId(itemId);
        }
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.isAvailable());
        item.setOwner(userService.get(idUser));
        return item;
    }
}
