package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
public class ItemService {

    private final ItemStorage<Item, Long> storage;
    private final UserService userService;

    public ItemService(ItemStorage<Item, Long> storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public Item save(Item item, Long idUser) {
        return storage.save(item);
    }

    public Item patch(Item item, Long idItem) {
        return storage.update(item, idItem);
    }

    public Item get(Long id, Long idUser) {
        userService.get(idUser);
        return storage.get(id);
    }

    public void delete(Long id, Long idUser) {
        userService.get(idUser);
        storage.delete(id);
    }

    public List<Item> getAll(Long idUser) {
        userService.get(idUser);
        return storage.getAll(idUser);
    }

    public List<Item> search(String text) {
        return storage.search(text);
    }
}
