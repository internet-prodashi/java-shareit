package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.comment.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.exception.ExceptionItemNotFound;
import ru.practicum.shareit.exception.ExceptionNotActiveBooking;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data_service.sql")
class TestItemService {
    private final ItemService itemService;
    private final ItemDtoRequestCreate itemDtoRequestCreate = new ItemDtoRequestCreate(
            "Название вещи",
            "Описание вещи",
            true,
            2L
    );
    private final ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = new ItemRequestDtoRequestCreate(
            "Описание запроса на вещь №5",
            LocalDateTime.now()
    );
    private final ItemDtoRequestUpdate itemDtoRequestUpdate = new ItemDtoRequestUpdate(
            "Название вещи №5 новое",
            "Описание вещи №5 новое",
            false,
            itemRequestDtoRequestCreate
    );

    @Test
    void createItem() {
        ItemDtoResponse itemDtoResponse = itemService.create(1L, itemDtoRequestCreate);
        assertThat(itemDtoResponse.name(), equalTo(itemDtoRequestCreate.name()));
        assertThat(itemDtoResponse.description(), equalTo(itemDtoRequestCreate.description()));
        assertThat(itemDtoResponse.available(), equalTo(itemDtoRequestCreate.available()));
    }

    @Test
    void updateItem() {
        ItemDtoResponse itemUpdate = itemService.update(5L, 5L, itemDtoRequestUpdate);
        assertThat(itemUpdate.name(), equalTo(itemDtoRequestUpdate.name()));
        assertThat(itemUpdate.description(), equalTo(itemDtoRequestUpdate.description()));
        assertThat(itemUpdate.available(), equalTo(itemDtoRequestUpdate.available()));
        assertThrows(ExceptionItemNotFound.class, () -> itemService.update(100L, 5L, itemDtoRequestUpdate));
    }

    @Test
    void deleteItem() {
        itemService.delete(5L);
        assertThrows(ExceptionItemNotFound.class, () -> itemService.getItem(5L));
    }

    @Test
    void searchItemsByName() {
        List<ItemDtoResponse> items = itemService.search("Название вещи №4");
        assertThat(items.size(), equalTo(1));
        assertThat(items.getFirst().name(), equalTo("Название вещи №4"));
    }

    @Test
    void createComment() {
        CommentDtoRequestCreate commentDtoRequestCreate = new CommentDtoRequestCreate(
                "Очень объемное финальное задание получается..."
        );
        CommentDtoResponse commentDtoResponse = itemService.createComment(2L, 1L, commentDtoRequestCreate);
        assertThat(commentDtoResponse.text(), equalTo(commentDtoRequestCreate.text()));
        assertThrows(ExceptionNotActiveBooking.class, () -> itemService.createComment(100L, 100L, commentDtoRequestCreate));
    }
}