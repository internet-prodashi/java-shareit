package ru.practicum.shareit.request.dto;

import lombok.Builder;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ItemRequestDtoListResponse(
        Long id,
        String description,
        LocalDateTime created,
        List<ItemDtoResponse> items
) {

    public static ItemRequestDtoListResponse create(ItemRequest itemRequest, List<ItemDtoResponse> items) {
        return ItemRequestDtoListResponse.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }
}
