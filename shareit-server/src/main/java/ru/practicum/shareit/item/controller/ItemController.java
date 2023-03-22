package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/items")
@Slf4j
public class ItemController {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto save(@RequestBody ItemMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Post Item with dto {}", dto);
        return itemService.save(dto, idUser);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemResponseDto patch(@RequestBody ItemMessageDto dto, @PathVariable long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Patch Item with dto {}", dto);
        Item item = ItemMapper.toEntity(dto, itemId);
        item = itemService.patch(item, itemId, idUser);
        return ItemMapper.toDto(item);
    }

    @GetMapping(value = "/{itemId}")
    public ItemResponseDto getItem(@PathVariable long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Get Item with {} user {}", itemId, idUser);
        return itemService.get(itemId, idUser);
    }

    @DeleteMapping(value = "/{itemId}")
    public void deleteItem(@PathVariable long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Delete Item with {} user {}", itemId, idUser);
        itemService.delete(itemId, idUser);
    }

    @GetMapping
    public List<ItemResponseDto> getAll(@RequestHeader(HEADER_REQUEST) long idUser,
                                        @RequestParam(name = "from", defaultValue = "0") int from,
                                        @RequestParam(name = "size", defaultValue = "99") int size) {
        log.info("Get all Item user {}", idUser);
        return itemService.getAll(idUser, from, size);
    }

    @GetMapping(value = "/search")
    public List<ItemResponseDto> search(@RequestParam String text,
                                        @RequestParam(name = "from", defaultValue = "0") int from,
                                        @RequestParam(name = "size", defaultValue = "99") int size) {
        log.info("Get Comment with search {} from {} size {}", text, from, size);
        return itemService.search(text, from, size).stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentResponseDto saveComment(@PathVariable long itemId, @RequestBody CommentMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Post Comment with dto {} itemId {} idUser {}", dto,itemId,idUser);
        Comment comment = CommentMapper.toEntity(dto);
        comment = itemService.saveComment(comment, idUser, itemId);
        return CommentMapper.toDto(comment);
    }
}
