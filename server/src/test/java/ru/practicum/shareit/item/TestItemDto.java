package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class TestItemDto {
    @Autowired
    private JacksonTester<ItemDtoRequestCreate> jacksonTester1;
    private final ItemDtoRequestCreate itemDtoRequestCreate = new ItemDtoRequestCreate(
            "Название вещи",
            "Описание вещи",
            true,
            1L
    );

    @Autowired
    private JacksonTester<ItemDtoRequestId> jacksonTester2;
    private final ItemDtoRequestId itemDtoRequestId = new ItemDtoRequestId(
            1L
    );

    @Autowired
    private JacksonTester<ItemDtoRequestUpdate> jacksonTester3;
    private final ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = new ItemRequestDtoRequestCreate(
            "Описание вещи",
            LocalDateTime.of(2025, 10, 16, 12, 12)
    );
    private final ItemDtoRequestUpdate itemDtoRequestUpdate = new ItemDtoRequestUpdate(
            "Название вещи",
            "Описание вещи",
            true,
            itemRequestDtoRequestCreate
    );

    @Autowired
    private JacksonTester<ItemDtoResponse> jacksonTester4;
    private final UserDtoResponse userDtoResponse = new UserDtoResponse(
            1L,
            "Пользователь №1",
            "mail@mail.ru"
    );
    private final ItemDtoResponse itemDtoResponse = new ItemDtoResponse(
            1L,
            "Название вещи",
            "Описание вещи",
            true,
            userDtoResponse,
            itemRequestDtoRequestCreate
    );

    @Autowired
    private JacksonTester<ItemDtoResponseWithBookings> jacksonTester5;
    private final BookingDtoResponse bookingDtoResponse1 = new BookingDtoResponse(
            1L,
            LocalDateTime.of(2025, 10, 16, 12, 12),
            LocalDateTime.of(2025, 11, 16, 12, 12),
            Status.WAITING,
            null,
            null
    );
    private final BookingDtoResponse bookingDtoResponse2 = new BookingDtoResponse(
            1L,
            LocalDateTime.of(2025, 10, 16, 12, 12),
            LocalDateTime.of(2025, 11, 16, 12, 12),
            Status.WAITING,
            null,
            null
    );
    private final ItemDtoResponseWithBookings itemDtoResponseWithBookings = new ItemDtoResponseWithBookings(
            1L,
            "Название вещи",
            "Описание вещи",
            true,
            userDtoResponse,
            null,
            bookingDtoResponse1,
            bookingDtoResponse2,
            null
    );

    @Test
    void itemDtoRequestCreate() throws Exception {
        JsonContent<ItemDtoRequestCreate> jsonContent = jacksonTester1.write(itemDtoRequestCreate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
    }

    @Test
    void itemDtoRequestId() throws Exception {
        JsonContent<ItemDtoRequestId> jsonContent = jacksonTester2.write(itemDtoRequestId);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    void itemDtoRequestUpdate() throws Exception {
        JsonContent<ItemDtoRequestUpdate> jsonContent = jacksonTester3.write(itemDtoRequestUpdate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.request.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.request.created").isEqualTo("2025-10-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
    }

    @Test
    void itemDtoResponse() throws Exception {
        JsonContent<ItemDtoResponse> jsonContent = jacksonTester4.write(itemDtoResponse);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.userDtoResponse.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.userDtoResponse.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.userDtoResponse.email").isEqualTo("mail@mail.ru");
        assertThat(jsonContent).extractingJsonPathStringValue("$.request.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.request.created").isEqualTo("2025-10-16T12:12:00");
    }

    @Test
    void itemDtoResponseWithBookings() throws Exception {
        JsonContent<ItemDtoResponseWithBookings> jsonContent = jacksonTester5.write(itemDtoResponseWithBookings);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.userResponseDto.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.userResponseDto.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.userResponseDto.email").isEqualTo("mail@mail.ru");

        assertThat(jsonContent).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.lastBooking.start").isEqualTo("2025-10-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.lastBooking.end").isEqualTo("2025-11-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.lastBooking.status").isEqualTo("WAITING");

        assertThat(jsonContent).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.nextBooking.start").isEqualTo("2025-10-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.nextBooking.end").isEqualTo("2025-11-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.nextBooking.status").isEqualTo("WAITING");
    }

}