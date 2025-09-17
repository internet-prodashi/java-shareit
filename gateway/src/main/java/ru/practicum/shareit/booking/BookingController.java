package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingState;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @RequestBody @Valid BookingDtoRequestCreate booking
    ) {
        return bookingClient.create(userId, booking);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @PathVariable @Positive Long bookingId,
            @RequestParam("approved") Boolean approved
    ) {
        return bookingClient.approve(userId, bookingId, approved);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @PathVariable @Positive Long bookingId
    ) {
        return bookingClient.get(userId, bookingId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getAllBookingCurrentUser(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        return bookingClient.getAllBookingCurrentUser(userId, state);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingAllItemCurrentUser(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        return bookingClient.getAllBookingAllItemCurrentUser(userId, state);
    }

}