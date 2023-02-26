package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.abstraction.PatchMap;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository storage;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PatchMap<Item> patchMap = new PatchMap<>();

    public Item save(Item item, Long idUser) {

        item.setOwner(userService.get(idUser));
        return storage.save(item);
    }

    @Transactional
    public Item patch(Item item, Long idItem, Long idUser) {
        item.setOwner(userService.get(idUser));
        Item itemFind = storage.findById(idItem).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "указанного предмета нет"));
        Long itemPatchOwnerId = item.getOwner().getId();
        Long itemOwnerId = itemFind.getOwner().getId();
        if (itemPatchOwnerId.equals(itemOwnerId)) {
            item = patchMap.patchObject(item, itemFind);
            return storage.save(item);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "другой пользователь");
        }
    }

    @Transactional
    public ItemResponseDto get(Long id, Long idUser) {
        userService.get(idUser);
        Item item = storage.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "указанного предмета нет"));
        ItemResponseDto dto = ItemMapper.toDto(item);
        if (item.getOwner().getId().equals(idUser)) {
            Booking nextBooking = bookingRepository.findTop1ByItemIdAndStartIsAfterAndStatusIsOrderByStartAsc(id, LocalDateTime.now(), Status.APPROVED);
            Booking lastBooking = bookingRepository.findTop1ByItemIdAndEndIsBeforeAndStatusIsOrderByStartDesc(id, LocalDateTime.now(), Status.APPROVED);
            dto.setNextBooking(nextBooking == null ? null : BookingMapper.toDto(nextBooking));
            dto.setLastBooking(lastBooking == null ? null : BookingMapper.toDto(lastBooking));
        }
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());
        List<CommentResponseDto> commentsDto = comments.stream().map(CommentMapper::toDto).collect(Collectors.toList());
        dto.setComments(commentsDto);
        return dto;
    }

    @Transactional
    public void delete(Long id, Long idUser) {
        userService.get(idUser);
        storage.deleteById(id);
    }

    @Transactional
    public List<ItemResponseDto> getAll(Long idUser) {
        userService.get(idUser);
        List<Item> items = storage.findAllByOwnerId(idUser);
        List<ItemResponseDto> dto = items.stream().sorted((o1, o2) -> o1.getId().compareTo(o2.getId())).map(ItemMapper::toDto).collect(Collectors.toList());
        for (ItemResponseDto dtoItemBooking : dto) {
            Booking nextBooking = bookingRepository.findTop1ByItemIdAndStartIsAfterAndStatusIsOrderByStartAsc(dtoItemBooking.getId(), LocalDateTime.now(), Status.APPROVED);
            Booking lastBooking = bookingRepository.findTop1ByItemIdAndEndIsBeforeAndStatusIsOrderByStartDesc(dtoItemBooking.getId(), LocalDateTime.now(), Status.APPROVED);
            dtoItemBooking.setNextBooking(nextBooking == null ? null : BookingMapper.toDto(nextBooking));
            dtoItemBooking.setLastBooking(lastBooking == null ? null : BookingMapper.toDto(lastBooking));
            List<Comment> comments = commentRepository.findAllByItemId(dtoItemBooking.getId());
            List<CommentResponseDto> commentsDto = comments.stream().map(CommentMapper::toDto).collect(Collectors.toList());
            dtoItemBooking.setComments(commentsDto);
        }
        return dto;
    }

    public List<Item> search(String text) {
        String search = text.toLowerCase();
        return storage.search(search);
    }

    @Transactional
    public Comment saveComment(Comment comment, Long idUser, Long itemId) {
        Item item = storage.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "указанного предмета нет"));
        User author = userRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "указанного пользователя нет"));
        Booking booking = bookingRepository.findTop1ByBookerIdAndItemIdAndEndIsBefore(idUser, itemId, LocalDateTime.now());
        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "брони пользователем не было");
        }
        comment.setItem(item);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }
}
