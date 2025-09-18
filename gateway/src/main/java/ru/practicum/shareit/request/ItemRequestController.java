package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @Valid @RequestBody ItemRequestDtoRequestCreate itemRequest
    ) {
        return itemRequestClient.create(userId, itemRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getAllRequestByUserId(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId
    ) {
        return itemRequestClient.getAllRequestByUserId(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @PathVariable @Positive Long requestId,
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId
    ) {
        return itemRequestClient.getRequestById(requestId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public ResponseEntity<Object> getAll(
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero int from,
            @RequestParam(defaultValue = "20", required = false) @Positive int quantity,
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId
    ) {
        return itemRequestClient.getAll(from, quantity, userId);
    }
}