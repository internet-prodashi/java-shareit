package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ExceptionUserEmailExists;
import ru.practicum.shareit.exception.ExceptionUserNotFound;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryInMemory implements UserRepository {
    private final Map<Long, User> storage = new HashMap<>();
    private Long id = 0L;

    @Override
    public User create(User user) {
        if (checkEmail(user.getEmail(), -1L))
            throw new ExceptionUserEmailExists("Электронная почта (" + user.getEmail() + ") уже существует.");
        user.setId(generateId());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public void update(User user) {
        if (checkEmail(user.getEmail(), user.getId()))
            throw new ExceptionUserEmailExists("Электронная почта (" + user.getEmail() + ") уже существует.");
        storage.put(user.getId(), user);
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    @Override
    public User get(Long id) {
        Optional<User> user = Optional.ofNullable(storage.get(id));
        user.orElseThrow(() -> new ExceptionUserNotFound("Пользователь с идентификатором (" + id + ") не найден."));
        return user.get();
    }

    private long generateId() {
        return id++;
    }

    private boolean checkEmail(String email, Long id) {
        return storage.values()
                .stream()
                .anyMatch(user -> !user.getId().equals(id) && user.getEmail().equalsIgnoreCase(email));
    }

}
