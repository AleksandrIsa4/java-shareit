package ru.practicum.shareit.request.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.abstraction.BaseModel;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest extends BaseModel<Long> {

    @Column(name = "description")
    String description;

    @OneToOne()
    @JoinColumn(name = "requestor_id")
    User requestor;

    @Column(name = "created_date")
    LocalDateTime created;
}
