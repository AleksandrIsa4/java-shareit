package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
@Slf4j
public class ItemRequestController {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    private final RequestService requestService;

    @PostMapping
    public ItemRequestResponceDto save(@Valid @RequestBody ItemRequestMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Post ItemRequest with dto {} user {} ", dto,idUser);
        return requestService.save(dto, idUser);
    }

    @GetMapping
    public List<ItemRequestResponceDto> getAllMy(@RequestHeader(HEADER_REQUEST) long idUser) {
        log.info("Get ItemRequest user {} ",idUser);
        return requestService.getAllMy(idUser);
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestResponceDto getRequest(@PathVariable() @NotNull Long requestId, @RequestHeader(HEADER_REQUEST) long idUser) {
        log.info("Get ItemRequest user {}  request {} ",idUser,requestId);
        return requestService.getRequest(idUser, requestId);
    }

    @GetMapping(value = "/all")
    public List<ItemRequestResponceDto> getAll(@RequestHeader(HEADER_REQUEST) long idUser,
                                               @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                               @RequestParam(name = "size", defaultValue = "99") @Min(1) int size) {
        log.info("Get ItemRequest user {}  from {} size {}",idUser,from,size);
        return requestService.getAllPagination(idUser, from, size);
    }
}
