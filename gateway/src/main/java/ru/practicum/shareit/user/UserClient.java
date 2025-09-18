package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;

@Service
public class UserClient extends BaseClient {
    @Autowired
    public UserClient(@Value("http://shareit-server:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/users"))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(UserDtoRequestCreate user) {
        return post("", user);
    }

    public ResponseEntity<Object> update(Long id, UserDtoRequestUpdate user) {
        return patch("/" + id, user);
    }

    public ResponseEntity<Object> delete(Long id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> get(Long id) {
        return get("/" + id);
    }

}