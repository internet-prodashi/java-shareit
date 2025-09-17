package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.ExceptionUserNotFound;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data_service.sql")
class TestUserService {
    private final UserService userService;
    UserDtoRequestCreate userDtoRequestCreate = new UserDtoRequestCreate(
            "Пользователь №6",
            "mail6@mail.ru"
    );

    @Test
    void createUser() {
        UserDtoResponse user = userService.create(userDtoRequestCreate);
        UserDtoResponse getUser = userService.get(user.id());
        assertThat(user, equalTo(getUser));
    }

    @Test
    void updateUser() {
        UserDtoRequestUpdate userUpdate = new UserDtoRequestUpdate(
                "Пользователь №1 новый",
                "mail1new@mail.ru"
        );
        UserDtoResponse userNew = userService.update(1L, userUpdate);
        assertThat(userNew.name(), equalTo("Пользователь №1 новый"));
        assertThat(userNew.email(), equalTo("mail1new@mail.ru"));
    }

    @Test
    void getUser() {
        UserDtoResponse userTwo = userService.get(2L);
        assertThat(userTwo.name(), equalTo("Пользователь №2"));
        assertThat(userTwo.email(), equalTo("mail2@mail.ru"));
    }

    @Test
    void deleteUser() {
        userService.delete(5L);
        assertThrows(ExceptionUserNotFound.class, () -> userService.get(5L));
    }
}