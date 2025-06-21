package developer.ezandro.forumhubapi.dto;

import developer.ezandro.forumhubapi.model.TopicStatus;

public record TopicUpdateRequestDTO(
        String title,
        String message,
        TopicStatus status) {
}