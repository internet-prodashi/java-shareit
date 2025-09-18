package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNull;

@ExtendWith(MockitoExtension.class)
class TestItemRequestClient {
    @Mock
    private RestTemplateBuilder builder;
    @Mock
    private RestTemplate restTemplate;
    private ItemRequestClient itemRequestClient;
    private final ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = new ItemRequestDtoRequestCreate(
            "Описание запроса на вещь №1",
            LocalDateTime.now()
    );

    @BeforeEach
    void setUp() {
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        itemRequestClient = new ItemRequestClient("", builder);
    }

    @Test
    void createItemRequest() {
        Mockito
                .when(restTemplate.exchange("", HttpMethod.POST, new HttpEntity<>(itemRequestDtoRequestCreate,
                        getHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(itemRequestDtoRequestCreate));
        ResponseEntity<Object> response = itemRequestClient.create(1L, itemRequestDtoRequestCreate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(itemRequestDtoRequestCreate));
    }

    @Test
    void getAllItemRequestByUserId() {
        List<ItemRequestDtoRequestCreate> requests = List.of(itemRequestDtoRequestCreate);
        Mockito
                .when(restTemplate.exchange("", HttpMethod.GET, new HttpEntity<>(null,
                        getHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(requests));
        ResponseEntity<Object> response = itemRequestClient.getAllRequestByUserId(1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(requests));
    }

    @Test
    void getItemRequestById() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.GET, new HttpEntity<>(null,
                        getHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(itemRequestDtoRequestCreate));
        ResponseEntity<Object> response = itemRequestClient.getRequestById(1L, 1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(itemRequestDtoRequestCreate));
    }

    @Test
    void getAllItemRequest() {
        Mockito
                .lenient().when(restTemplate.exchange("/all?from=0&quantity=20", HttpMethod.GET, new HttpEntity<>(null,
                        getHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(null));
        ResponseEntity<Object> response = itemRequestClient.getAll(0, 20, 2L);
        assertNull("Ответ должен быть null.", response);
    }

    private HttpHeaders getHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("X-Sharer-User-Id", String.valueOf(userId));
        return headers;
    }
}