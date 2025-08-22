package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ExceptionItemNotFound;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestUpdate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    public ItemDtoResponse create(Long userId, ItemDtoRequestCreate itemDtoRequestCreate) {
        UserDtoResponse user = userService.get(userId);
        Item item = itemMapper.toItemDtoRequestCreate(itemDtoRequestCreate, user, null);
        return itemMapper.toItemDtoResponse(itemRepository.create(item));
    }

    public ItemDtoResponse update(Long itemId, Long userId, ItemDtoRequestUpdate itemNew) {
        Item itemOld = itemRepository.get(itemId);
        if (!Objects.equals(itemOld.getOwner().getId(), userId)) {
            throw new ExceptionItemNotFound("Вещь с идентификатором (" + itemId + ") не найдена для пользователя с идентификатором (" + userId + ").");
        }
        itemMapper.itemDtoRequestUpdate(itemNew, itemOld);
        itemRepository.update(itemOld);
        return itemMapper.toItemDtoResponse(itemOld);
    }

    public void delete(Long itemId) {
        itemRepository.get(itemId);
        itemRepository.delete(itemId);
    }

    public ItemDtoResponse get(Long itemId) {
        return itemMapper.toItemDtoResponse(itemRepository.get(itemId));
    }

    public List<ItemDtoResponse> getByUserId(Long userId) {
        userService.get(userId);
        return itemMapper.toListItemDtoResponse(itemRepository.getByUserId(userId));
    }

    public List<ItemDtoResponse> search(String text) {
        return itemMapper.toListItemDtoResponse(itemRepository.search(text));
    }
}
