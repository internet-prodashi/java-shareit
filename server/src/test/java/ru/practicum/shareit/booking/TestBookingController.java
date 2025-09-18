package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.contoller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
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


@WebMvcTest(controllers = BookingController.class)
public class TestBookingController {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    BookingService bookingService;
    private final BookingDtoRequestCreate bookingDtoRequestCreate = new BookingDtoRequestCreate(
            1L,
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusDays(1)
    );
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
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusDays(1),
            Status.WAITING,
            itemDtoResponse,
            userDtoResponse
    );

    @Test
    void createBooking() throws Exception {
        Mockito
                .when(bookingService.create(1L, bookingDtoRequestCreate))
                .thenReturn(bookingDtoResponse);
        bookingService.create(1L, bookingDtoRequestCreate);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDtoRequestCreate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void approveBooking() throws Exception {
        when(bookingService.approve(1L, 1L, true))
                .thenReturn(bookingDtoResponse);
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingDtoRequestCreate))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.id()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.status().toString())));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.get(1L, 1L))
                .thenReturn(bookingDtoResponse);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.id()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.status().toString())));
    }

    @Test
    void getAllBookingCurrentUser() throws Exception {
        when(bookingService.getAllBookingCurrentUser(1L, BookingState.ALL))
                .thenReturn(List.of(bookingDtoResponse));
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.id()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDtoResponse.status().toString())));
    }

    @Test
    void getAllBookingAllItemCurrentUser() throws Exception {
        when(bookingService.getAllBookingAllItemCurrentUser(1L, BookingState.ALL))
                .thenReturn(List.of(bookingDtoResponse));
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.id()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDtoResponse.status().toString())));
    }

}