package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class TestUserDto {
    @Autowired
    private JacksonTester<UserDtoRequestCreate> jacksonTester1;
    private final UserDtoRequestCreate userDtoRequestCreate = new UserDtoRequestCreate(
            "Пользователь №1",
            "mail@mail.ru"
    );

    @Autowired
    private JacksonTester<UserDtoRequestUpdate> jacksonTester2;
    private final UserDtoRequestUpdate userDtoRequestUpdate = new UserDtoRequestUpdate(
            "Пользователь №1",
            "mail@mail.ru"
    );

    @Autowired
    private JacksonTester<UserDtoResponse> jacksonTester3;
    private final UserDtoResponse userDtoResponse = new UserDtoResponse(
            1L,
            "Пользователь №1",
            "mail@mail.ru"
    );

    @Test
    void userDtoRequestCreate() throws Exception {
        JsonContent<UserDtoRequestCreate> jsonContent = jacksonTester1.write(userDtoRequestCreate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("mail@mail.ru");
    }

    @Test
    void userDtoRequestUpdate() throws Exception {
        JsonContent<UserDtoRequestUpdate> jsonContent = jacksonTester2.write(userDtoRequestUpdate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("mail@mail.ru");
    }

    @Test
    void userDtoResponse() throws Exception {
        JsonContent<UserDtoResponse> jsonContent = jacksonTester3.write(userDtoResponse);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("mail@mail.ru");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

}