package ru.practicum.shareit.item.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.abstraction.PatchMap;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class InMemoryItemStorage implements ItemStorage<Item, Long> {

    private long generator = 0;
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, HashMap<Long, Item>> userItems = new HashMap<>();
    private final PatchMap<Item> patchMap = new PatchMap<>();

    @Override
    public Item save(Item item) {
        item.setId(++generator);
        items.put(item.getId(), item);
        HashMap<Long, Item> itemsUser = userItems.computeIfAbsent(item.getOwner().getId(), k -> new HashMap<>());
        itemsUser.put(item.getId(), item);
        userItems.put(item.getOwner().getId(), itemsUser);
        return items.get(item.getId());
    }

    @Override
    public Item update(Item item, Long id) {
        //  item=patchMap.patchObject2(item,get(id));
        long itemPatchId = item.getId();
        long itemPatchOwnerId = item.getOwner().getId();
        long itemOwnerId = get(id).getOwner().getId();
        if ((itemPatchId == id) && (itemPatchOwnerId == itemOwnerId)) {
            item = patchMap.patchObject(item, get(id));
            items.put(item.getId(), item);
            HashMap<Long, Item> itemsUser = userItems.computeIfAbsent(item.getOwner().getId(), k -> new HashMap<>());
            itemsUser.put(item.getId(), item);
            userItems.put(item.getOwner().getId(), itemsUser);
            return item;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "не совпадает пользователь и его предмет");
        }
    }

    @Override
    public Item get(Long id) {
        checkItemId(id);
        return items.get(id);
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }

    @Override
    public List<Item> getAll(Long id) {
        List<Item> values = new ArrayList<>();
        userItems.get(id).entrySet().stream().forEach(entry -> {
            values.add(entry.getValue());
        });
        return values;
    }

    @Override
    public List<Item> search(String text) {
        String search = text.toLowerCase();
        List<Item> values = new ArrayList<>();
        String searchName;
        String searchDescription;
        for (Item item : items.values()) {
            if (item.isAvailable()) {
                searchName = item.getName().toLowerCase();
                searchDescription = item.getDescription().toLowerCase();
                if (searchDescription.contains(search) || searchName.contains(search)) {
                    values.add(item);
                }
            }
        }
        return values;
    }

    public void checkItemId(Long id) {
        if (!(items.containsKey(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "указанного предмета нет");
        }
    }
}
