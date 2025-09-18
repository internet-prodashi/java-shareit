package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ExceptionUserNotFound;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDtoResponse create(UserDtoRequestCreate user) {
        return userMapper.toUserDtoResponse(userRepository.save(userMapper.toUserDtoRequestCreate(user)));
    }

    public UserDtoResponse update(Long id, UserDtoRequestUpdate userNew) {
        User userOld = getUser(id);
        userMapper.userDtoRequestUpdate(userNew, userOld);
        userRepository.save(userOld);
        return userMapper.toUserDtoResponse(userOld);
    }

    public void delete(Long id) {
        getUser(id);
        userRepository.deleteById(id);
    }

    public UserDtoResponse get(Long id) {
        return userMapper.toUserDtoResponse(getUser(id));
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ExceptionUserNotFound("Пользователь с идентификатором (" + id + ") не найден."));
    }

}