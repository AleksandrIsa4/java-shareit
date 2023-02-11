package ru.practicum.shareit.item.controller;

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
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemResponseDto save(@Valid @RequestBody ItemMessageDto dto, @RequestHeader(
            value = "X-Sharer-User-Id",
            required = true)
    long idUser) {
        Item item = itemMapper.toEntity(dto, idUser, null);
        item = itemService.save(item, idUser);
        return itemMapper.toDto(item);
    }

    @PatchMapping(value = "/{itemId}")

    public ItemResponseDto patch(@RequestBody ItemMessageDto dto, @PathVariable("itemId") @NotNull Long itemId, @RequestHeader(
            value = "X-Sharer-User-Id",
            required = true)
    long idUser) {
        Item item = itemMapper.toEntity(dto, idUser, itemId);
        item = itemService.patch(item, itemId);
        return itemMapper.toDto(item);

    }

    @GetMapping(value = "/{itemId}")
    public ItemResponseDto getItem(@PathVariable("itemId") @NotNull Long itemId, @RequestHeader(
            value = "X-Sharer-User-Id",
            required = true)
    long idUser) {
        Item item = itemService.get(itemId, idUser);
        return itemMapper.toDto(item);
    }

    @DeleteMapping(value = "/{itemId}")
    public void deleteItem(@PathVariable("itemId") @NotNull Long itemId, @RequestHeader(
            value = "X-Sharer-User-Id",
            required = true)
    long idUser) {
        itemService.delete(itemId, idUser);
    }

    @GetMapping
    public List<ItemResponseDto> getAll(@RequestHeader(
            value = "X-Sharer-User-Id",
            required = true)
                                        long idUser) {
        return itemService.getAll(idUser).stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/search")
    public List<ItemResponseDto> search(@RequestParam String text, @RequestHeader(
            value = "X-Sharer-User-Id",
            required = true)
    long idUser) {
        if (text == null || text.isEmpty() || text.trim().isEmpty()) {
            return new ArrayList<>();
        } else {
            //   return new ArrayList<>();
            return itemService.search(text).stream().map(ItemMapper::toDto).collect(Collectors.toList());
        }
    }
}
