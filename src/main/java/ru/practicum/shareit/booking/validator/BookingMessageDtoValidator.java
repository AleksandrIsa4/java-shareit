package ru.practicum.shareit.booking.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingMessageDto;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;

@Validated
@Component
public class BookingMessageDtoValidator implements Validator<BookingMessageDto> {
    public void check(@Valid BookingMessageDto bookingDto) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "время конца раньше времени начала");
        }
    }
}
