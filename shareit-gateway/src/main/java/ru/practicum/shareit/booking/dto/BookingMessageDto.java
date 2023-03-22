package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.abstraction.BaseModel;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class BookingMessageDto extends BaseModel<Long> {

    Long itemId;
    @FutureOrPresent
    @NotNull
    LocalDateTime start;
    @Future
    @NotNull
    LocalDateTime end;
}