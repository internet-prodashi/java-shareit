package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class TestUserController {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserClient userClient;
    @Autowired
    private MockMvc mvc;
    private final UserDtoResponse userDtoResponse = new UserDtoResponse(
            1L,
            "Пользователь №1",
            "mail@mail.ru"
    );
    private final UserDtoRequestCreate userDtoRequestCreate = new UserDtoRequestCreate(
            "Пользователь №1",
            "mail@mail.ru"
    );
    private final UserDtoRequestUpdate userDtoRequestUpdate = new UserDtoRequestUpdate(
            "Пользователь №1 обновлен",
            "mail@mail.ru"
    );

    @Test
    void createUser() throws Exception {
        when(userClient.create(userDtoRequestCreate))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoRequestCreate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser() throws Exception {
        when(userClient.update(1L, userDtoRequestUpdate))
                .thenReturn(ResponseEntity.ok(userDtoRequestUpdate));
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoRequestUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDtoRequestUpdate.name())))
                .andExpect(jsonPath("$.email", is(userDtoRequestUpdate.email())));
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getUser() throws Exception {
        when(userClient.get(1L))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDtoResponse.name())))
                .andExpect(jsonPath("$.email", is(userDtoResponse.email())));
    }
}