package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestUpdate;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @Validated @RequestBody ItemDtoRequestCreate item
    ) {
        return itemClient.create(userId, item);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{itemId}")
    public ResponseEntity<?> update(
            @PathVariable @Positive Long itemId,
            @Validated @RequestBody ItemDtoRequestUpdate item,
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId
    ) {
        return itemClient.update(itemId, userId, item);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> delete(
            @PathVariable @Positive Long itemId
    ) {
        return itemClient.delete(itemId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long requesterId,
            @PathVariable @Positive Long itemId
    ) {
        return itemClient.getById(requesterId, itemId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getByUserId(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId
    ) {
        return itemClient.getByUserId(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam(name = "text") String text
    ) {
        if (!StringUtils.hasText(text))
            return null;
        return itemClient.search(text);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @PathVariable("itemId") @Positive Long itemId,
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @Valid @RequestBody CommentDtoRequestCreate comment
    ) {
        return itemClient.createComment(userId, itemId, comment);
    }

}