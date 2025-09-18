package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestId;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class TestItemRequestDto {
    @Autowired
    private JacksonTester<ItemRequestDtoRequestCreate> jacksonTester1;
    private final ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = new ItemRequestDtoRequestCreate(
            "Описание вещи",
            LocalDateTime.of(2025, 10, 16, 12, 12)
    );

    @Autowired
    private JacksonTester<ItemRequestDtoRequestId> jacksonTester2;
    private final ItemRequestDtoRequestId itemRequestDtoRequestId = new ItemRequestDtoRequestId(
            1L
    );

    @Autowired
    private JacksonTester<ItemRequestDtoResponse> jacksonTester3;
    private final ItemRequestDtoResponse itemRequestDtoResponse = new ItemRequestDtoResponse(
            1L,
            "Описание вещи",
            LocalDateTime.of(2025, 10, 16, 12, 12)
    );

    @Test
    void itemRequestDtoRequestCreate() throws Exception {
        JsonContent<ItemRequestDtoRequestCreate> jsonContent = jacksonTester1.write(itemRequestDtoRequestCreate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo("2025-10-16T12:12:00");
    }

    @Test
    void itemRequestDtoRequestId() throws Exception {
        JsonContent<ItemRequestDtoRequestId> jsonContent = jacksonTester2.write(itemRequestDtoRequestId);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void itemRequestDtoResponse() throws Exception {
        JsonContent<ItemRequestDtoResponse> jsonContent = jacksonTester3.write(itemRequestDtoResponse);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo("2025-10-16T12:12:00");
    }

}