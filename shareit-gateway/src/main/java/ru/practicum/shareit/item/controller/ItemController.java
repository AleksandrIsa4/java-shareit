package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.ItemMessageDto;
import ru.practicum.shareit.item.dto.CommentMessageDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    static final String HEADER_REQUEST = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody ItemMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Post Item with dto {}", dto);
        return itemClient.postItem(dto, idUser);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> patch(@RequestBody ItemMessageDto dto, @PathVariable @NotNull long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Patch Item with dto {}", dto);
        return itemClient.patchItem(dto, idUser, itemId);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable @NotNull long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Get Item with {} user {}", itemId, idUser);
        return itemClient.getItem(itemId, idUser);
    }

    @DeleteMapping(value = "/{itemId}")
    public void deleteItem(@PathVariable @NotNull long itemId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Delete Item with {} user {}", itemId, idUser);
        itemClient.deleteItem(itemId, idUser);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(HEADER_REQUEST) long idUser,
                                         @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(name = "size", defaultValue = "99") @Positive int size) {
        log.info("Get all Item user {}", idUser);
        return itemClient.getItems(idUser, from, size);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(name = "size", defaultValue = "99") @Positive int size) {
        log.info("Get Comment with search {} from {} size {}", text,from,size);
        if (text == null || text.isEmpty() || text.trim().isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            return itemClient.searchItems(text, from, size);
        }
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@PathVariable @NotNull Long itemId, @Valid @RequestBody CommentMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Post Comment with dto {} itemId {} idUser {}", dto,itemId,idUser);
        return itemClient.saveComment(dto, idUser, itemId);
    }
}
