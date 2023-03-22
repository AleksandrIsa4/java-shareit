package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
@Slf4j
public class ItemRequestController {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody ItemRequestMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Post ItemRequest with dto {} user {} ", dto,idUser);
        return requestClient.postRequest(dto, idUser);
    }

    @GetMapping
    public ResponseEntity<Object> getAllMy(@RequestHeader(HEADER_REQUEST) long idUser) {
        log.info("Get ItemRequest user {} ",idUser);
        return requestClient.getMyRequest(idUser);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable @NotNull long requestId, @RequestHeader(HEADER_REQUEST) long idUser) {
        log.info("Get ItemRequest user {}  request {} ",idUser,requestId);
        return requestClient.getRequest(idUser, requestId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAll(@RequestHeader(HEADER_REQUEST) long idUser,
                                         @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(name = "size", defaultValue = "99") @Positive int size) {
        log.info("Get ItemRequest user {}  from {} size {}",idUser,from,size);
        return requestClient.getAll(idUser, from, size);
    }
}
