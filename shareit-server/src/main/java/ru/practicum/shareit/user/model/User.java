package ru.practicum.shareit.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.abstraction.BaseModel;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "users", schema = "public")
public class User extends BaseModel<Long> {

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "email", unique = true)
    String email;
}
