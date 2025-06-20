package developer.ezandro.forumhubapi.dto;

import java.time.LocalDateTime;

public record TopicResponseDTO(
        Long id,
        String title,
        String message,
        LocalDateTime creationDate,
        String status,
        String authorName,
        String courseName) {
}