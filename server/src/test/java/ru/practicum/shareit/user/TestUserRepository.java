package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TestUserRepository {
    private final User user = User.builder()
            .name("Имя пользователя")
            .email("mail@mail.ru")
            .build();
    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback
    void createUser() {
        User current = userRepository.save(user);
        assertThat(current).isEqualTo(user);
    }

    @Test
    @Rollback
    void deleteUser() {
        User current = userRepository.save(user);
        userRepository.delete(current);
        Optional<User> searchUser = userRepository.findById(1L);
        Assertions.assertFalse(searchUser.isPresent());
    }

    @Test
    @Rollback
    void findUser() {
        User current = userRepository.save(user);
        Optional<User> searchUser = userRepository.findById(1L);
        assertThat(current).isEqualTo(searchUser.get());
    }

}