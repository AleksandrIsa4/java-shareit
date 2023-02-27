package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(" SELECT i FROM Item i WHERE (lower(i.description) LIKE concat('%', :text, '%') OR lower(i.name) LIKE concat('%', :text, '%')) AND i.available=true")
    List<Item> search(String text);

    List<Item> findAllByOwnerId(Long userId);
}