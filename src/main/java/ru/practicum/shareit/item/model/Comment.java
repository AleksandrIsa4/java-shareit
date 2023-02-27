package ru.practicum.shareit.item.model;

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
@Table(name = "comments", schema = "public")
public class Comment extends BaseModel<Long> {

    @Column(name = "text")
    String text;

    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @OneToOne
    @JoinColumn(name = "author_id")
    User author;

    @Column(name = "created_date")
    private LocalDateTime created;
}
