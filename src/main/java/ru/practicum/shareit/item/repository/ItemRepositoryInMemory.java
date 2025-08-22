package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ExceptionItemNotFound;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryInMemory implements ItemRepository {
    private final Map<Long, Item> storage = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item create(Item item) {
        item.setId(generateId());
        storage.put(item.getId(), item);
        return item;
    }

    @Override
    public void update(Item item) {
        storage.put(item.getId(), item);
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    @Override
    public Item get(Long id) {
        Optional<Item> item = Optional.ofNullable(storage.get(id));
        item.orElseThrow(() -> new ExceptionItemNotFound("Вещь с идентификатором (" + id + ") не найдена."));
        return item.get();
    }

    @Override
    public List<Item> getByUserId(Long userId) {
        return storage.values()
                .stream()
                .filter(item -> item.getOwner() != null && Objects.equals(item.getOwner().getId(), userId))
                .toList();
    }

    @Override
    public List<Item> search(String name) {
        return storage.values()
                .stream()
                .filter(Item::isAvailable)
                .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(name.toLowerCase())) ||
                                (item.getDescription() != null && item.getDescription().toLowerCase().contains(name.toLowerCase())))
                .toList();
    }

    private long generateId() {
        return id++;
    }

}