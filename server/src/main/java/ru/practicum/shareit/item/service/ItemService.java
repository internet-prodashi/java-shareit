package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.ExceptionItemNotFound;
import ru.practicum.shareit.exception.ExceptionNotActiveBooking;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestUpdate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoResponseWithBookings;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ItemRequestService itemRequestService;

    public ItemDtoResponse create(Long userId, ItemDtoRequestCreate itemDtoRequestCreate) {
        User user = userService.getUser(userId);
        ItemRequest itemRequest = null;
        if (itemDtoRequestCreate.requestId() != null)
            itemRequest = itemRequestService.getItemRequest(itemDtoRequestCreate.requestId());

        Item item = itemMapper.toItemDtoRequestCreate(itemDtoRequestCreate, user, itemRequest);
        return itemMapper.toItemDtoResponse(itemRepository.save(item));
    }

    public ItemDtoResponse update(Long itemId, Long userId, ItemDtoRequestUpdate itemNew) {
        Item itemOld = getItem(itemId);
        if (!Objects.equals(itemOld.getOwner().getId(), userId)) {
            throw new ExceptionItemNotFound("Вещь с идентификатором (" + itemId + ") не найдена для пользователя с идентификатором (" + userId + ").");
        }
        itemMapper.itemDtoRequestUpdate(itemNew, itemOld);
        itemRepository.save(itemOld);
        return itemMapper.toItemDtoResponse(itemOld);
    }

    public void delete(Long itemId) {
        getItem(itemId);
        itemRepository.deleteById(itemId);
    }

    public ItemDtoResponseWithBookings getById(Long requesterId, Long itemId) {
        Item item = itemRepository.findByIdWithComments(itemId)
                .orElseThrow(() -> new ExceptionItemNotFound("Вещь с идентификатором (" + itemId + ") не найдена"));
        Booking last = null;
        Booking next = null;
        if (item.getOwner() != null && item.getOwner().getId().equals(requesterId)) {
            LocalDateTime now = LocalDateTime.now();
            last = bookingRepository
                    .findFirstByItemIdAndStartBeforeAndEndBeforeOrderByEndDesc(itemId, now, now)
                    .orElse(null);
            next = bookingRepository
                    .findFirstByItemIdAndStartAfterOrderByStartAsc(itemId, now)
                    .orElse(null);
        }
        return itemMapper.toItemDtoResponseWithBookings(item, last, next);
    }

    public List<ItemDtoResponse> getByUserId(Long userId) {
        userService.getUser(userId);
        return itemMapper.toListItemDtoResponse(itemRepository.findByOwnerId(userId));
    }

    public List<ItemDtoResponse> search(String text) {
        return itemMapper.toListItemDtoResponse(itemRepository.findItemsByNameContainingIgnoreCaseAndAvailable(text, true));
    }

    public CommentDtoResponse createComment(Long userId, Long itemId, CommentDtoRequestCreate comment) {
        boolean isNotActiveBooking = bookingRepository.existsByBookerIdAndItemIdAndStartBeforeAndStatusNotIn(
                userId,
                itemId,
                LocalDateTime.now(),
                List.of(Status.REJECTED, Status.WAITING, Status.CANCELED)
        );
        if (!isNotActiveBooking)
            throw new ExceptionNotActiveBooking("У пользователя с идентификатором " + userId +
                                                " не активно бронирование вещи с идентификатором " + itemId);
        User user = userService.getUser(userId);
        Item item = getItem(itemId);
        Comment entity = commentMapper.toCommentDtoRequestCreate(comment.text(), item, user);
        Comment saved = commentRepository.save(entity);
        return commentMapper.toCommentDtoResponse(saved);
    }

    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ExceptionItemNotFound("Вещь с идентификатором (" + itemId + ") не найдена."));
    }

}
