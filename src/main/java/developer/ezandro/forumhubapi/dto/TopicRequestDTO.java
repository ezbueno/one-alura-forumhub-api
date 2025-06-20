package developer.ezandro.forumhubapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicRequestDTO(
        @NotBlank(message = "Title must not be blank")
        String title,

        @NotBlank(message = "Message must not be blank")
        String message,

        @NotNull(message = "Course id must not be null")
        Long courseId) {
}