package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMessageDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
@Slf4j
public class BookingController {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto save(@RequestBody BookingMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Post Booking with dto {} idUser {}", dto, idUser);
        Booking booking = BookingMapper.toEntity(dto);
        Long idItem = dto.getItemId();
        booking = bookingService.save(booking, idUser, idItem);
        return BookingMapper.toDto(booking);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingResponseDto patch(@PathVariable Long bookingId, @RequestHeader(HEADER_REQUEST)
    long idUser, @RequestParam boolean approved) {
        log.info("Patch Booking with bookingId {} idUser {} approved {}", bookingId, idUser, approved);
        Booking booking = bookingService.patch(approved, bookingId, idUser);
        return BookingMapper.toDto(booking);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingResponseDto getBooking(@PathVariable Long bookingId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Get Booking with bookingId {} idUser {}", bookingId, idUser);
        Booking booking = bookingService.get(bookingId, idUser);
        return BookingMapper.toDto(booking);
    }

    @GetMapping()
    public List<BookingResponseDto> findAllByStateBooker(@RequestHeader(HEADER_REQUEST) long idUser,
                                                         @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                         @RequestParam(name = "from", defaultValue = "0") int from,
                                                         @RequestParam(name = "size", defaultValue = "99") int size) {
        log.info("Get Booking with idUser {} state {} from {} size {}", idUser, state, from,size);
        return bookingService.getAllBooker(idUser, State.valueOf(state), from, size).stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/owner")
    public List<BookingResponseDto> findAllByStateOwner(@RequestHeader(HEADER_REQUEST) long idUser,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                        @RequestParam(name = "from", defaultValue = "0") int from,
                                                        @RequestParam(name = "size", defaultValue = "99") int size) {
        log.info("Get owner Booking with idUser {} state {} from {} size {}", idUser, state, from,size);
        return bookingService.getAllOwner(idUser, State.valueOf(state), from, size).stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }
}
