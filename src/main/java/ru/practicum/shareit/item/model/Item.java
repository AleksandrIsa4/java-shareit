package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.abstraction.BaseModel;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "items", schema = "public")
public class Item extends BaseModel<Long> {

    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "available")
    Boolean available;

    @OneToOne()
    @JoinColumn(name = "owner_id")
    User owner;
}
