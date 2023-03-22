package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.abstraction.BaseModel;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserMessageDto extends BaseModel<Long> {

    String name;
    String email;
}
