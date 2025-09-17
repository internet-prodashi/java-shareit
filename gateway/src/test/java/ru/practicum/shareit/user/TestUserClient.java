package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.List;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestUserClient {
    @Mock
    private RestTemplateBuilder builder;
    @Mock
    private RestTemplate restTemplate;
    private UserClient userClient;
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

    @BeforeEach
    void setUp() {
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        userClient = new UserClient("", builder);
    }

    @Test
    void createUser() {
        Mockito
                .when(restTemplate.exchange("", HttpMethod.POST, new HttpEntity<>(userDtoRequestCreate,
                        getHeaders(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = userClient.create(userDtoRequestCreate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void updateUser() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.PATCH, new HttpEntity<>(userDtoRequestUpdate,
                        getHeaders(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = userClient.update(1L, userDtoRequestUpdate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void deleteUser() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.DELETE, new HttpEntity<>(null,
                        getHeaders(null)), Object.class))
                .thenReturn(ResponseEntity.ok().build());
        ResponseEntity<Object> response = userClient.delete(1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    void getUser() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.GET, new HttpEntity<>(null,
                        getHeaders(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = userClient.get(1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    private HttpHeaders getHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null)
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        return headers;
    }
}