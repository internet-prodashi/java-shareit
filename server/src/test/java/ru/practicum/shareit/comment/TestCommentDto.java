package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.comment.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class TestCommentDto {
    @Autowired
    private JacksonTester<CommentDtoRequestCreate> jacksonTester1;
    private final CommentDtoRequestCreate commentDtoRequestCreate = new CommentDtoRequestCreate(
            "Текст комментария"
    );

    @Autowired
    private JacksonTester<CommentDtoResponse> jacksonTester2;
    private final CommentDtoResponse commentDtoResponse = new CommentDtoResponse(
            1L,
            "Текст комментария",
            "Автор комментария",
            LocalDateTime.of(2025, 10, 16, 12, 12)
    );

    @Test
    void commentDtoRequestCreate() throws Exception {
        JsonContent<CommentDtoRequestCreate> jsonContent = jacksonTester1.write(commentDtoRequestCreate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.text").isEqualTo("Текст комментария");
    }

    @Test
    void commentDtoResponse() throws Exception {
        JsonContent<CommentDtoResponse> jsonContent = jacksonTester2.write(commentDtoResponse);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.text").isEqualTo("Текст комментария");
        assertThat(jsonContent).extractingJsonPathStringValue("$.authorName").isEqualTo("Автор комментария");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo("2025-10-16T12:12:00");
    }

}