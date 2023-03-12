package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMessageDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enumeration.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    private final BookingService bookingService;

    private final Validator<BookingMessageDto> bookingDtoValidator;

    @PostMapping
    public BookingResponseDto save(@Valid @RequestBody BookingMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        bookingDtoValidator.check(dto);
        Booking booking = BookingMapper.toEntity(dto);
        Long idItem = dto.getItemId();
        booking = bookingService.save(booking, idUser, idItem);
        return BookingMapper.toDto(booking);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingResponseDto patch(@PathVariable() @NotNull Long bookingId, @RequestHeader(HEADER_REQUEST)
    long idUser, @RequestParam @NotNull boolean approved) {
        Booking booking = bookingService.patch(approved, bookingId, idUser);
        return BookingMapper.toDto(booking);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingResponseDto getBooking(@PathVariable() @NotNull Long bookingId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        Booking booking = bookingService.get(bookingId, idUser);
        return BookingMapper.toDto(booking);
    }

    @GetMapping()
    public List<BookingResponseDto> findAllByStateBooker(@RequestHeader(HEADER_REQUEST) long idUser,
                                                         @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                         @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                         @RequestParam(name = "size", defaultValue = "99") @Min(1) Integer size) {
        try {
            return bookingService.getAllBooker(idUser, State.valueOf(state), from, size).stream().map(BookingMapper::toDto).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    @GetMapping(value = "/owner")
    public List<BookingResponseDto> findAllByStateOwner(@RequestHeader(HEADER_REQUEST) long idUser,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                        @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                        @RequestParam(name = "size", defaultValue = "99") @Min(1) Integer size) {
        try {
            return bookingService.getAllOwner(idUser, State.valueOf(state), from, size).stream().map(BookingMapper::toDto).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
