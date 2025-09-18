package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestUpdate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoResponseWithBookings;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, BookingMapper.class,
        CommentMapper.class, ItemRequestMapper.class})
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "available", source = "item.available")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "request", source = "request")
    Item toItemDtoRequestCreate(ItemDtoRequestCreate item, User owner, ItemRequest request);

    @Mapping(target = "request", ignore = true)
    ItemDtoResponse toItemDtoResponse(Item item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void itemDtoRequestUpdate(ItemDtoRequestUpdate itemDtoRequestUpdate, @MappingTarget Item item);

    List<ItemDtoResponse> toListItemDtoResponse(List<Item> items);

    @Mapping(target = "id", source = "item.id")
    ItemDtoResponseWithBookings toItemDtoResponseWithBookings(Item item, Booking last, Booking next);
}
