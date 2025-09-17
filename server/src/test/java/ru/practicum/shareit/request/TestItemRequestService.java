package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.ExceptionUserNotFound;
import ru.practicum.shareit.request.dto.ItemRequestDtoListResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data_service.sql")
class TestItemRequestService {
    private final ItemRequestService itemRequestService;
    private final ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = new ItemRequestDtoRequestCreate(
            "Новый запрос на поиск вещи",
            LocalDateTime.now()
    );

    @Test
    void createItemRequest() {
        ItemRequestDtoResponse itemRequestDtoResponse = itemRequestService.create(1L, itemRequestDtoRequestCreate);
        assertThat(itemRequestDtoRequestCreate.description(), equalTo(itemRequestDtoResponse.description()));
        assertThrows(ExceptionUserNotFound.class, () ->
                itemRequestService.create(100L, itemRequestDtoRequestCreate));
    }

    @Test
    void getAllItemRequestByUserId() {
        List<ItemRequestDtoListResponse> listItemRequest = itemRequestService.getAllRequestByUserId(5L);
        assertThat(listItemRequest.size(), equalTo(1));
        assertThat(listItemRequest.getFirst().id(), equalTo(5L));
        assertThrows(ExceptionUserNotFound.class, () ->
                itemRequestService.getAllRequestByUserId(100L));
    }

    @Test
    void getAllItemRequest() {
        List<ItemRequestDtoListResponse> listItemRequest = itemRequestService.getAll(0, 20, 4L);
        assertThat(listItemRequest.size(), equalTo(4));
    }
}