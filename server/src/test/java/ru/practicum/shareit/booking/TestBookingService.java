package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ExceptionBookingForbiddenOperation;
import ru.practicum.shareit.exception.ExceptionBookingNotFound;
import ru.practicum.shareit.exception.ExceptionBookingNotOwner;
import ru.practicum.shareit.exception.ExceptionUserNotFound;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data_service.sql")
class TestBookingService {
    private final BookingService bookingService;
    private final BookingDtoRequestCreate bookingDtoRequestCreate = new BookingDtoRequestCreate(
            1L,
            LocalDateTime.parse("2025-09-01T01:00:00"),
            LocalDateTime.parse("2025-12-01T01:00:00")
    );

    @Test
    void createBooking() {
        BookingDtoResponse bookingDtoResponse = bookingService.create(1L, bookingDtoRequestCreate);
        assertThat(bookingDtoResponse.start(), equalTo(bookingDtoRequestCreate.start()));
        assertThat(bookingDtoResponse.end(), equalTo(bookingDtoRequestCreate.end()));
        assertThrows(ExceptionUserNotFound.class, () -> bookingService.create(100L, bookingDtoRequestCreate));
    }

    @Test
    void approveBooking() {
        BookingDtoResponse bookingDtoResponse = bookingService.approve(2L, 2L, true);
        assertThat(bookingDtoResponse.status(), equalTo(Status.APPROVED));
        assertThrows(ExceptionBookingNotOwner.class, () -> bookingService.approve(300L, 2L, true));
        assertThrows(ExceptionBookingNotFound.class, () -> bookingService.approve(3L, 200L, true));
    }

    @Test
    void getBooking() {
        BookingDtoResponse bookingDtoResponse = bookingService.get(2L, 5L);
        assertThat(bookingDtoResponse.status(), equalTo(Status.CANCELED));
        assertThrows(ExceptionBookingNotFound.class, () -> bookingService.get(1L, 100L));
        assertThrows(ExceptionBookingForbiddenOperation.class, () -> bookingService.get(100L, 1L));
    }

    @Test
    void getAllBookingCurrentUser() {
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllBookingCurrentUser(2L, BookingState.ALL);
        assertThat(bookingDtoResponse.getFirst().status(), equalTo(Status.CANCELED));
        assertThat(bookingDtoResponse.size(), equalTo(2));
        assertThrows(ExceptionUserNotFound.class, () -> bookingService.getAllBookingCurrentUser(100L, BookingState.ALL));
    }

}