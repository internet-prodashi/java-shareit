package ru.practicum.shareit.request.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "itemRequest.description")
    @Mapping(target = "created", source = "itemRequest.created")
    @Mapping(target = "requestor", source = "requestor")
    ItemRequest toItemRequestDtoRequestCreate(ItemRequestDtoRequestCreate itemRequest, User requestor);

    ItemRequestDtoResponse toItemRequestDtoResponse(ItemRequest itemRequest);

    List<ItemRequestDtoResponse> toListItemRequestDtoResponse(List<ItemRequest> itemsRequest);

}