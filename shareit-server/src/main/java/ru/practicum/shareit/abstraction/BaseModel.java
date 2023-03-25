package ru.practicum.shareit.abstraction;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseModel<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected T id;

    public BaseModel() {
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
