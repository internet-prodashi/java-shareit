package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
class TestClient {
    @Mock
    private RestTemplate restTemplate;
    private BaseClient baseClient;
    private final UserDtoResponse userDtoResponse = new UserDtoResponse(
            1L,
            "Пользователь №1",
            "mail@mail.ru"
    );

    @BeforeEach
    void setUp() {
        baseClient = new BaseClient(restTemplate);
    }

    @Test
    void getPath() {
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.GET, new HttpEntity<>(null,
                        getHeader(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.get("/");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void getPathAndUserId() {
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.GET, new HttpEntity<>(null,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.get("/", 1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void getPathAndUserIdAndParameters() {
        Map<String, Object> params = Map.of("State", "All");
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.GET, new HttpEntity<>(null,
                        getHeader(1L)), Object.class, params))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.get("/", 1L, params);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void postPathAndBody() {
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.POST, new HttpEntity<>(userDtoResponse,
                        getHeader(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.post("/", userDtoResponse);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void postPathAndUserIdAndBody() {
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.POST, new HttpEntity<>(userDtoResponse,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.post("/", 1L, userDtoResponse);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void postPathAndUserIdAndParametersAndBody() {
        Map<String, Object> params = Map.of("State", "All");
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.POST, new HttpEntity<>(userDtoResponse,
                        getHeader(1L)), Object.class, params))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.post("/", 1L, params, userDtoResponse);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void putPathAndUserIdAndBody() {
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.PUT, new HttpEntity<>(userDtoResponse,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.put("/", 1L, userDtoResponse);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void putPathAndUserIdAndParametersAndBody() {
        Map<String, Object> params = Map.of("State", "All");
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.PUT, new HttpEntity<>(userDtoResponse,
                        getHeader(1L)), Object.class, params))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.put("/", 1L, params, userDtoResponse);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void patchPathAndBody() {
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.PATCH, new HttpEntity<>(userDtoResponse,
                        getHeader(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.patch("/", userDtoResponse);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void patchPathAndUserId() {
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.PATCH, new HttpEntity<>(null,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.patch("/", 1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void patchPathAndUserIdAndBody() {
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.PATCH, new HttpEntity<>(userDtoResponse,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.patch("/", 1L, userDtoResponse);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void patchPathAndUserIdAndParametersAndBody() {
        Map<String, Object> params = Map.of("State", "All");
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.PATCH, new HttpEntity<>(userDtoResponse,
                        getHeader(1L)), Object.class, params))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.patch("/", 1L, params, userDtoResponse);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void deletePath() {
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.DELETE, new HttpEntity<>(null,
                        getHeader(null)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.delete("/");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void deletePathAndUserId() {
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.DELETE, new HttpEntity<>(null,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.delete("/", 1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    @Test
    void deletePathAndUserIdAndParameters() {
        Map<String, Object> params = Map.of("State", "All");
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.DELETE, new HttpEntity<>(null,
                        getHeader(1L)), Object.class, params))
                .thenReturn(ResponseEntity.ok(userDtoResponse));
        ResponseEntity<Object> response = baseClient.delete("/", 1L, params);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(userDtoResponse));
    }

    private HttpHeaders getHeader(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null)
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        return headers;
    }
}