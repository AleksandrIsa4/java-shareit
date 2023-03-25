package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingMessageDto;
import ru.practicum.shareit.booking.dto.enumeration.State;
import ru.practicum.shareit.booking.validator.Validator;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
@Slf4j
public class BookingController {

    static final String HEADER_REQUEST = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    private final Validator<BookingMessageDto> bookingDtoValidator;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody BookingMessageDto dto, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Post Booking with dto {} idUser {}", dto, idUser);
        bookingDtoValidator.check(dto);
        return bookingClient.postBooking(dto, idUser);
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> patch(@PathVariable @NotNull long bookingId, @RequestHeader(HEADER_REQUEST)
    long idUser, @RequestParam @NotNull boolean approved) {
        log.info("Patch Booking with bookingId {} idUser {} approved {}", bookingId, idUser, approved);
        return bookingClient.patchBooking(idUser, bookingId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable @NotNull long bookingId, @RequestHeader(HEADER_REQUEST)
    long idUser) {
        log.info("Get Booking with bookingId {} idUser {}", bookingId, idUser);
        return bookingClient.getBooking(idUser, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> findAllByStateBooker(@RequestHeader(HEADER_REQUEST) long idUser,
                                                       @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                       @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                       @RequestParam(name = "size", defaultValue = "99") @Positive Integer size) {
        log.info("Get Booking with idUser {} state {} from {} size {}", idUser, state, from,size);
        try {
            return bookingClient.findAllByStateBooker(idUser, State.valueOf(state), from, size);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }


    @GetMapping(value = "/owner")
    public ResponseEntity<Object> findAllByStateOwner(@RequestHeader(HEADER_REQUEST) long idUser,
                                                      @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                      @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                      @RequestParam(name = "size", defaultValue = "99") @Positive Integer size) {
        log.info("Get owner Booking with idUser {} state {} from {} size {}", idUser, state, from,size);
        try {
            return bookingClient.findAllByStateOwner(idUser, State.valueOf(state), from, size);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
