package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    User create(User user);

    void update(User user);

    void delete(Long id);

    User get(Long id);
}