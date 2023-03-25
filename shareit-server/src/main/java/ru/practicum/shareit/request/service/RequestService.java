package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestMessageDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    public ItemRequestResponceDto save(ItemRequestMessageDto dto, Long idUser) {
        User requestor = userService.get(idUser);
        ItemRequest itemRequest = RequestMapper.toEntity(dto);
        itemRequest.setRequestor(requestor);
        itemRequest = itemRequestRepository.save(itemRequest);
        return RequestMapper.toDto(itemRequest);
    }

    public List<ItemRequestResponceDto> getAllMy(Long idUser) {
        User requestor = userService.get(idUser);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(requestor.getId());
        List<ItemRequestResponceDto> itemRequestResponceDtos = itemRequests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
        for (ItemRequestResponceDto dto : itemRequestResponceDtos) {
            List<Item> items = itemRepository.findAllByRequestId(dto.getId());
            List<ItemResponseDto> itemResponseDtos = items.stream().map(ItemMapper::toDto).collect(Collectors.toList());
            dto.setItems(itemResponseDtos);
        }
        return itemRequestResponceDtos;
    }

    public ItemRequestResponceDto getRequest(Long idUser, Long requestId) {
        userService.get(idUser);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "указанного запроса нет"));
        ItemRequestResponceDto itemRequestDto = RequestMapper.toDto(itemRequest);
        List<Item> items = itemRepository.findAllByRequestId(itemRequestDto.getId());
        List<ItemResponseDto> itemResponseDtos = items.stream().map(ItemMapper::toDto).collect(Collectors.toList());
        itemRequestDto.setItems(itemResponseDtos);
        return itemRequestDto;
    }

    public List<ItemRequestResponceDto> getAllPagination(Long idUser, Integer from, Integer size) {
        userService.get(idUser);
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());
        List<ItemRequest> pageRequest = itemRequestRepository.findAllRequest(idUser, pageable);
        List<ItemRequestResponceDto> itemRequestResponceDtos = pageRequest.stream().map(RequestMapper::toDto).collect(Collectors.toList());
        for (ItemRequestResponceDto dto : itemRequestResponceDtos) {
            List<Item> items = itemRepository.findAllByRequestId(dto.getId());
            List<ItemResponseDto> itemResponseDtos = items.stream().map(ItemMapper::toDto).collect(Collectors.toList());
            dto.setItems(itemResponseDtos);
        }
        return itemRequestResponceDtos;
    }
}