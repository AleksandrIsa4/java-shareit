package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentMessageDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/items")
public class ItemController {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto save(@Valid @RequestBody ItemMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        return itemService.save(dto, idUser);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemResponseDto patch(@RequestBody ItemMessageDto dto, @PathVariable() @NotNull Long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        Item item = ItemMapper.toEntity(dto, itemId);
        item = itemService.patch(item, itemId, idUser);
        return ItemMapper.toDto(item);
    }

    @GetMapping(value = "/{itemId}")
    public ItemResponseDto getItem(@PathVariable() @NotNull Long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        return itemService.get(itemId, idUser);
    }

    @DeleteMapping(value = "/{itemId}")
    public void deleteItem(@PathVariable() @NotNull Long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        itemService.delete(itemId, idUser);
    }

    @GetMapping
    public List<ItemResponseDto> getAll(@RequestHeader(HEADER_REQUEST) long idUser,
                                        @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                        @RequestParam(name = "size", defaultValue = "99") @Min(1) int size) {
        return itemService.getAll(idUser, from, size);
    }

    @GetMapping(value = "/search")
    public List<ItemResponseDto> search(@RequestParam String text,
                                        @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                        @RequestParam(name = "size", defaultValue = "99") @Min(1) int size) {
        if (text == null || text.isEmpty() || text.trim().isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemService.search(text, from, size).stream().map(ItemMapper::toDto).collect(Collectors.toList());
        }
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentResponseDto saveComment(@PathVariable() @NotNull Long itemId, @Valid @RequestBody CommentMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        Comment comment = CommentMapper.toEntity(dto);
        comment = itemService.saveComment(comment, idUser, itemId);
        return CommentMapper.toDto(comment);
    }
}
