package ru.practicum.shareit.item.storage;

import java.util.List;

public interface ItemStorage<T, K> {

    T save(T data);

    T update(T data, K id);

    T get(K id);

    void delete(K id);

    List<T> getAll(K id);

    List<T> search(String text);
}
