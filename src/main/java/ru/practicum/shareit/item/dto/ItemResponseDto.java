package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.abstraction.BaseModel;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ItemResponseDto extends BaseModel<Long> {

    String name;
    String description;
    Boolean available;

    BookingResponseDto lastBooking;

    BookingResponseDto nextBooking;

    List<CommentResponseDto> comments;
}
