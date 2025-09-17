package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.Status;
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

@ExtendWith(MockitoExtension.class)
public class TestBookingClient {
    @Mock
    private RestTemplateBuilder builder;
    @Mock
    private RestTemplate restTemplate;
    private BookingClient bookingClient;
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

    @BeforeEach
    void setUp() {
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        bookingClient = new BookingClient("", builder);
    }

    @Test
    void createBooking() {
        Mockito
                .when(restTemplate.exchange("", HttpMethod.POST, new HttpEntity<>(bookingDtoRequestCreate,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(bookingDtoResponse));
        ResponseEntity<Object> response = bookingClient.create(1L, bookingDtoRequestCreate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(bookingDtoResponse));
    }

    @Test
    void approveBooking() {
        Mockito
                .when(restTemplate.exchange("/1?approved=true", HttpMethod.PATCH, new HttpEntity<>(null,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(bookingDtoResponse));
        ResponseEntity<Object> response = bookingClient.approve(1L, 1L, true);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(bookingDtoResponse));
    }

    @Test
    void getBooking() {
        Mockito
                .when(restTemplate.exchange("/1", HttpMethod.GET, new HttpEntity<>(null,
                        getHeader(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(bookingDtoResponse));
        ResponseEntity<Object> response = bookingClient.get(1L, 1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(bookingDtoResponse));
    }

    @Test
    void getAllBookingCurrentUser() {
        Map<String, Object> parameters = Map.of(
                "state", BookingState.WAITING.name()
        );
        List<BookingDtoResponse> bookings = List.of(bookingDtoResponse);
        Mockito
                .when(restTemplate.exchange("?state={state}", HttpMethod.GET, new HttpEntity<>(null,
                        getHeader(1L)), Object.class, parameters))
                .thenReturn(ResponseEntity.ok(bookings));
        ResponseEntity<Object> response = bookingClient.getAllBookingCurrentUser(1L, BookingState.WAITING);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(bookings));
    }

    private HttpHeaders getHeader(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("X-Sharer-User-Id", String.valueOf(userId));
        return headers;
    }

}
