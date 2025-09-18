package ru.practicum.shareit.request.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoListResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemRequestDtoResponse create(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestBody ItemRequestDtoRequestCreate itemRequest
    ) {
        return itemRequestService.create(userId, itemRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemRequestDtoListResponse> getAllRequestByUserId(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllRequestByUserId(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{requestId}")
    public ItemRequestDtoListResponse getRequestById(
            @PathVariable long requestId,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId
    ) {
        return itemRequestService.getRequestById(requestId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public List<ItemRequestDtoListResponse> getAll(
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "20", required = false) int quantity,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId
    ) {
        return itemRequestService.getAll(from, quantity, userId);
    }

}
