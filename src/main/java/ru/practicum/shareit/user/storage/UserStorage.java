package ru.practicum.shareit.user.storage;

import java.util.List;

public interface UserStorage<T,K> {

    T save(T data);

    T update(T data,K id);

    T get(K id);

    void delete(K id);

    List<T> getAll();
}
