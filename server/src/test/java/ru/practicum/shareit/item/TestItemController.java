package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class TestItemController {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    ItemService itemService;
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
    private final ItemDtoResponseWithBookings itemDtoResponseWithBookings = new ItemDtoResponseWithBookings(
            1L,
            "Название вещи",
            "Описание вещи",
            false,
            userDtoResponse,
            null,
            null,
            null,
            null
    );
    private final CommentDtoResponse commentDtoResponse = new CommentDtoResponse(
            1L,
            "Текст комментария",
            "Автор комментария",
            LocalDateTime.now()
    );

    @Test
    void createItem() throws Exception {
        when(itemService.create(1L, itemDtoRequestCreate))
                .thenReturn(itemDtoResponse);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoRequestCreate))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.update(1L, 1L, itemDtoRequestUpdate))
                .thenReturn(itemDtoResponse);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDtoRequestUpdate))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDtoResponse.name())))
                .andExpect(jsonPath("$.description", is(itemDtoResponse.description())));
    }

    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/1").characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getById(1L, 1L))
                .thenReturn(itemDtoResponseWithBookings);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDtoRequestUpdate.name())))
                .andExpect(jsonPath("$.description", is(itemDtoRequestUpdate.description())));
    }

    @Test
    void getItemByUserId() throws Exception {
        when(itemService.getByUserId(1L))
                .thenReturn(List.of(itemDtoResponse));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemDtoRequestUpdate.name())))
                .andExpect(jsonPath("$[0].description", is(itemDtoRequestUpdate.description())));

    }

    @Test
    void searchItem() throws Exception {
        when(itemService.search("Название вещи"))
                .thenReturn(List.of(itemDtoResponse));
        mvc.perform(get("/items/search?text=Название вещи").characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemDtoRequestUpdate.name())))
                .andExpect(jsonPath("$[0].description", is(itemDtoRequestUpdate.description())));
    }

    @Test
    void createComment() throws Exception {
        when(itemService.createComment(1L, 1L, commentDtoRequestCreate))
                .thenReturn(commentDtoResponse);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDtoRequestCreate))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

}