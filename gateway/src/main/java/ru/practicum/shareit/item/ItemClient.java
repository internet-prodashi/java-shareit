package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestUpdate;

@Slf4j
@Service
public class ItemClient extends BaseClient {
    @Autowired
    public ItemClient(@Value("http://shareit-server:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/items"))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long userId, ItemDtoRequestCreate item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> update(Long itemId, Long userId, ItemDtoRequestUpdate item) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> delete(Long itemId) {
        return delete("/" + itemId);
    }

    public ResponseEntity<Object> getById(Long requesterId, Long itemId) {
        return get("/" + itemId, requesterId);
    }

    public ResponseEntity<Object> getByUserId(Long userId) {
        return get("/", userId);
    }

    public ResponseEntity<Object> search(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentDtoRequestCreate comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}