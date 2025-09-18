package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ExceptionItemRequestNotFound;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoListResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserService userService;
    private final ItemMapper itemMapper;

    public ItemRequestDtoResponse create(Long userId, ItemRequestDtoRequestCreate itemRequestDtoRequestCreate) {
        User user = userService.getUser(userId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequestDtoRequestCreate(itemRequestDtoRequestCreate, user);
        return itemRequestMapper.toItemRequestDtoResponse(itemRequestRepository.save(itemRequest));
    }

    public List<ItemRequestDtoListResponse> getAllRequestByUserId(Long userId) {
        userService.getUser(userId);
        if (itemRequestRepository.findAllByRequestor_idOrderByCreatedAsc(userId).isEmpty())
            return List.of();

        return itemRequestRepository.findAllByRequestor_idOrderByCreatedAsc(userId)
                .stream()
                .map(ir -> ItemRequestDtoListResponse.create(ir, itemMapper.toListItemDtoResponse(
                        itemRepository.findAllByOwnerIdOrderByIdAsc(ir.getRequestor().getId())
                )))
                .collect(Collectors.toList());
    }

    public ItemRequestDtoListResponse getRequestById(Long requestId, Long userId) {
        userService.getUser(userId);
        ItemRequest itemRequest = getItemRequest(requestId);
        List<Item> items = itemRepository.findAllByRequest_idOrderByIdAsc(requestId);
        List<ItemDtoResponse> item = itemMapper.toListItemDtoResponse(items);
        return ItemRequestDtoListResponse.create(itemRequest, item);
    }

    public List<ItemRequestDtoListResponse> getAll(int from, int quantity, long userId) {
        userService.getUser(userId);
        Pageable pageable = PageRequest.of(from, quantity, Sort.by(Sort.Direction.DESC, "created"));
        return itemRequestRepository.findAllByRequestor_idNotIn(List.of(userId), pageable).getContent()
                .stream()
                .map(ir -> ItemRequestDtoListResponse.create(ir, itemMapper.toListItemDtoResponse(
                        itemRepository.findAllByOwnerIdOrderByIdAsc(ir.getRequestor().getId()))))
                .collect(Collectors.toList());
    }

    public ItemRequest getItemRequest(Long id) {
        return itemRequestRepository.findById(id)
                .orElseThrow(() -> new ExceptionItemRequestNotFound("Запрос вещи с идентификатором (" + id + ") не найден."));
    }

}
