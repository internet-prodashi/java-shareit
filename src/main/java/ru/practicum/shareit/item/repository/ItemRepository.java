package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item create(Item item);

    void update(Item item);

    void delete(Long id);

    Item get(Long id);

    List<Item> getByUserId(Long ownerId);

    List<Item> search(String name);
}