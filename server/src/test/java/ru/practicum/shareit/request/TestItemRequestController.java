package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.contoller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDtoListResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemRequestController.class)
public class TestItemRequestController {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    ItemRequestService itemRequestService;
    private final ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = new ItemRequestDtoRequestCreate(
            "Описание запроса на вещь №1",
            LocalDateTime.now()
    );
    private final ItemRequestDtoResponse itemRequestDtoResponse = new ItemRequestDtoResponse(
            1L,
            "Описание запроса на вещь №1",
            LocalDateTime.now()
    );
    private final ItemRequestDtoListResponse itemRequestDtoListResponse = new ItemRequestDtoListResponse(
            1L,
            "Описание запроса на вещь №1",
            LocalDateTime.now(),
            null
    );

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.create(1L, itemRequestDtoRequestCreate))
                .thenReturn(itemRequestDtoResponse);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoRequestCreate))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.description", is(itemRequestDtoResponse.description())));
    }

    @Test
    void getAllItemRequestByUserId() throws Exception {
        when(itemRequestService.getAllRequestByUserId(1L))
                .thenReturn(List.of(itemRequestDtoListResponse));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(itemRequestDtoRequestCreate.description())));
    }

    @Test
    void getAllItemRequestById() throws Exception {
        when(itemRequestService.getRequestById(1L, 1L))
                .thenReturn(itemRequestDtoListResponse);
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllItemRequest() throws Exception {
        when(itemRequestService.getAll(0, 20, 1L))
                .thenReturn(List.of(itemRequestDtoListResponse));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}