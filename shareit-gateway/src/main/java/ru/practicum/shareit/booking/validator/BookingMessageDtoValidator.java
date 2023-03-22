package ru.practicum.shareit.booking.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingMessageDto;

import javax.validation.Valid;

@Validated
@Component
public class BookingMessageDtoValidator implements Validator<BookingMessageDto> {
    public void check(@Valid BookingMessageDto bookingDto) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "время конца раньше времени начала");
        }
        if (bookingDto.getEnd().equals(bookingDto.getStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "время конца не должно быть равно времени начала");
        }
    }
}
