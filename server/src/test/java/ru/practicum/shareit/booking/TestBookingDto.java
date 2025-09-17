package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class TestBookingDto {
    @Autowired
    private JacksonTester<BookingDtoRequestCreate> jacksonTester1;
    private final BookingDtoRequestCreate bookingDtoRequestCreate = new BookingDtoRequestCreate(
            1L,
            LocalDateTime.of(2025, 10, 16, 12, 12),
            LocalDateTime.of(2025, 11, 16, 12, 12)
    );

    @Autowired
    private JacksonTester<BookingDtoResponse> jacksonTester2;
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
    private final BookingDtoResponse bookingDtoResponse = new BookingDtoResponse(
            1L,
            LocalDateTime.of(2025, 10, 16, 12, 12),
            LocalDateTime.of(2025, 11, 16, 12, 12),
            Status.WAITING,
            itemDtoResponse,
            userDtoResponse
    );

    @Test
    void bookingDtoRequestCreate() throws Exception {
        JsonContent<BookingDtoRequestCreate> jsonContent = jacksonTester1.write(bookingDtoRequestCreate);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo("2025-10-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo("2025-11-16T12:12:00");
    }

    @Test
    void bookingDtoResponse() throws Exception {
        JsonContent<BookingDtoResponse> jsonContent = jacksonTester2.write(bookingDtoResponse);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo("2025-10-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo("2025-11-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.email").isEqualTo("mail@mail.ru");
    }

}