package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.comment.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestUpdate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNull;

@ExtendWith(MockitoExtension.class)
class TestItemClient {
    @Mock
    private RestTemplateBuilder builder;
    @Mock
    private RestTemplate restTemplate;
    private ItemClient itemClient;
    private final UserDtoResponse userDtoResponse = new UserDtoResponse(
            1L,
            "Пользователь №1",
            "mail@mail.ru"
    );
    private final ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = new ItemRequestDtoRequestCreate(
            "Описание запроса на вещь №1",
            LocalDateTime.now()
    );
    private final ItemDtoResponse itemDtoResponse = new ItemDtoResponse(
            1L,
            "Название вещи",
            "Описание вещи",
            true,
            userDtoResponse,
            itemRequestDtoRequestCreate
    );
    private final ItemDtoRequestCreate itemDtoRequestCreate = new ItemDtoRequestCreate(
            "Название вещи",
            "Описание вещи",
            true,
            2L
    );
    private final ItemDtoRequestUpdate itemDtoRequestUpdate = new ItemDtoRequestUpdate(
            "Название вещи",
            "Описание вещи",
            false,
            itemRequestDtoRequestCreate
    );
    private final CommentDtoRequestCreate commentDtoRequestCreate = new CommentDtoRequestCreate(
            "Текст комментария"
    );

    @BeforeEach
    void setUp() {
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        itemClient = new ItemClient("", builder);
    }

    @Test
    void createItem() {
        Mockito
                .when(restTemplate.exchange("", HttpMethod.POST, new HttpEntity<>(itemDtoRequestCreate,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(itemDtoResponse));
        ResponseEntity<Object> response = itemClient.create(1L, itemDtoRequestCreate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(itemDtoResponse));
    }

    @Test
    void updateItem() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.PATCH, new HttpEntity<>(itemDtoRequestUpdate,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(itemDtoResponse));
        ResponseEntity<Object> response = itemClient.update(1L, 1L, itemDtoRequestUpdate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(itemDtoResponse));
    }

    @Test
    void deleteItem() {
        Mockito
                .lenient().when(restTemplate.exchange("/1", HttpMethod.PATCH, new HttpEntity<>(null,
                        getHeader(null)), Object.class))
                .thenReturn(ResponseEntity.ok(null));
        ResponseEntity<Object> response = itemClient.delete(1L);
        assertNull("Ответ должен быть null.", response);
    }

    @Test
    void getItemById() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.GET, new HttpEntity<>(null,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(itemDtoResponse));
        ResponseEntity<Object> response = itemClient.getById(1L, 1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(itemDtoResponse));
    }

    @Test
    void getItemByUserId() {
        List<ItemDtoResponse> items = List.of(itemDtoResponse);
        Mockito
                .when(restTemplate.exchange("/", HttpMethod.GET, new HttpEntity<>(null,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(items));
        ResponseEntity<Object> response = itemClient.getByUserId(1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(items));
    }

    @Test
    void searchItem() {
        Map<String, Object> parameters = Map.of(
                "text", "Новый поиск"
        );
        Mockito
                .lenient().when(restTemplate.exchange("/search?text={text}", HttpMethod.GET, new HttpEntity<>(null,
                        getHeader(null)), Object.class, parameters))
                .thenReturn(ResponseEntity.ok(null));
        ResponseEntity<Object> response = itemClient.search("Новый поиск");
        assertNull("Ответ должен быть null.", response);
    }

    @Test
    void createComment() {
        Mockito
                .when(restTemplate.exchange("/1/comment", HttpMethod.POST, new HttpEntity<>(commentDtoRequestCreate,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(null));
        ResponseEntity<Object> response = itemClient.createComment(1L, 1L, commentDtoRequestCreate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(null));
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