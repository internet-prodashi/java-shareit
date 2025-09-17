package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    @Autowired
    public ItemRequestClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/requests"))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long userId, ItemRequestDtoRequestCreate itemRequest) {
        return post("", userId, itemRequest);
    }

    public ResponseEntity<Object> getAllRequestByUserId(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getRequestById(long requestId, Long userId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getAll(int from, int quantity, Long userId) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "quantity", quantity);

        return get("/all?from={from}&quantity={quantity}", userId, parameters);
    }

}