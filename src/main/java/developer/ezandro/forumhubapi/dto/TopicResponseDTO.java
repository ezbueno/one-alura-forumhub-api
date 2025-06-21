package developer.ezandro.forumhubapi.dto;

import developer.ezandro.forumhubapi.model.Topic;

import java.time.LocalDateTime;

public record TopicResponseDTO(
        Long id,
        String title,
        String message,
        LocalDateTime creationDate,
        String status,
        String authorName,
        String courseName) {

    public TopicResponseDTO(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getCreationDate(),topic.getStatus().toString(), topic.getAuthor().getName(), topic.getCourse().getName());
    }
}