package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/items")
public class ItemController {

    private final String HEADER_REQUEST = "X-Sharer-User-Id";
    @Autowired
    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto save(@Valid @RequestBody ItemMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        Item item = ItemMapper.toEntity(dto, null);
        item = itemService.save(item, idUser);
        return ItemMapper.toDto(item);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemResponseDto patch(@RequestBody ItemMessageDto dto, @PathVariable("itemId") @NotNull Long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        Item item = ItemMapper.toEntity(dto, itemId);
        item = itemService.patch(item, itemId, idUser);
        return ItemMapper.toDto(item);
    }

    @GetMapping(value = "/{itemId}")
    public ItemResponseDto getItem(@PathVariable("itemId") @NotNull Long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        Item item = itemService.get(itemId, idUser);
        return ItemMapper.toDto(item);
    }

    @DeleteMapping(value = "/{itemId}")
    public void deleteItem(@PathVariable("itemId") @NotNull Long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        itemService.delete(itemId, idUser);
    }

    @GetMapping
    public List<ItemResponseDto> getAll(@RequestHeader(HEADER_REQUEST) long idUser) {
        return itemService.getAll(idUser).stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/search")
    public List<ItemResponseDto> search(@RequestParam String text) {
        if (text == null || text.isEmpty() || text.trim().isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemService.search(text).stream().map(ItemMapper::toDto).collect(Collectors.toList());
        }
    }
}
