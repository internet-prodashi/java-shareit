package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        return userMapper.toUserDtoResponse(userRepository.create(userMapper.toUserDtoRequestCreate(user)));
    }

    public UserDtoResponse update(Long id, UserDtoRequestUpdate userNew) {
        User userOld = userRepository.get(id);
        userMapper.userDtoRequestUpdate(userNew, userOld);
        userRepository.update(userOld);
        return userMapper.toUserDtoResponse(userOld);
    }

    public void delete(Long id) {
        userRepository.get(id);
        userRepository.delete(id);
    }

    public UserDtoResponse get(Long id) {
        return userMapper.toUserDtoResponse(userRepository.get(id));
    }

}