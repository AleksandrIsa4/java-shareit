package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.State;
import ru.practicum.shareit.booking.model.enumeration.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository storage;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Transactional
    public Booking save(Booking booking, Long idUser, Long idItem) {
        Item item = itemRepository.findById(idItem).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "указанного предмета нет"));
        long idItemOwner = item.getOwner().getId();
        if (idUser.equals(idItemOwner)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "пользователь является владельцем");
        }
        if (!item.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "предмет не доступен");
        }
        booking.setBooker(userService.get(idUser));
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return storage.save(booking);
    }

    @Transactional
    public Booking patch(boolean approved, Long bookingId, Long idUser) {
        userService.get(idUser);
        Booking booking = get(bookingId, idUser);
        Long itemPatchOwnerId = booking.getItem().getOwner().getId();
        if (!idUser.equals(itemPatchOwnerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не владелец");
        }
        if ((booking.getStatus().equals(Status.APPROVED) && approved)
                || (booking.getStatus().equals(Status.REJECTED) && !approved)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Статус предмета уже изменен");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        storage.save(booking);
        return get(bookingId, idUser);
    }

    public Booking get(Long bookingId, Long idUser) {
        Booking booking = storage.findById(bookingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "указанной брони нет"));
        Long bookingBookerId = booking.getBooker().getId();
        Long bookingItemOwnerId = booking.getItem().getOwner().getId();
        if (bookingBookerId.equals(idUser) || bookingItemOwnerId.equals(idUser)) {
            return booking;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "другой пользователь");
        }
    }

    @Transactional
    public List<Booking> getAllBooker(Long idUser, State state) {
        userService.get(idUser);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bokkingState = new ArrayList<>();
        switch (state) {
            case ALL:
                bokkingState = storage.findAllByBookerIdOrderByStartDesc(idUser);
                break;
            case CURRENT:
                bokkingState = storage.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(idUser, now, now);
                break;
            case PAST:
                bokkingState = storage.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(idUser, now);
                break;
            case FUTURE:
                bokkingState = storage.findAllByBookerIdAndStartIsAfterOrderByStartDesc(idUser, now);
                break;
            case WAITING:
                bokkingState = storage.findAllByBookerIdAndStatusIsOrderByStartDesc(idUser, Status.WAITING);
                break;
            case REJECTED:
                bokkingState = storage.findAllByBookerIdAndStatusIsOrderByStartDesc(idUser, Status.REJECTED);
                break;
        }
        return bokkingState;
    }

    @Transactional
    public List<Booking> getAllOwner(Long idUser, State state) {
        userService.get(idUser);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bokkingState = new ArrayList<>();
        switch (state) {
            case ALL:
                bokkingState = storage.findAllByItemOwnerIdOrderByStartDesc(idUser);
                break;
            case CURRENT:
                bokkingState = storage.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(idUser, now, now);
                break;
            case PAST:
                bokkingState = storage.findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(idUser, now);
                break;
            case FUTURE:
                bokkingState = storage.findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(idUser, now);
                break;
            case WAITING:
                bokkingState = storage.findAllByItemOwnerIdAndStatusIsOrderByStartDesc(idUser, Status.WAITING);
                break;
            case REJECTED:
                bokkingState = storage.findAllByItemOwnerIdAndStatusIsOrderByStartDesc(idUser, Status.REJECTED);
                break;
        }
        return bokkingState;
    }
}
