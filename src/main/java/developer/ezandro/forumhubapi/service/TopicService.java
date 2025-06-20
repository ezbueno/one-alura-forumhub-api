package developer.ezandro.forumhubapi.service;

import developer.ezandro.forumhubapi.dto.TopicRequestDTO;
import developer.ezandro.forumhubapi.dto.TopicResponseDTO;
import developer.ezandro.forumhubapi.exception.CourseNotFoundException;
import developer.ezandro.forumhubapi.exception.UserNotFoundException;
import developer.ezandro.forumhubapi.model.Course;
import developer.ezandro.forumhubapi.model.Topic;
import developer.ezandro.forumhubapi.model.TopicStatus;
import developer.ezandro.forumhubapi.model.User;
import developer.ezandro.forumhubapi.repository.CourseRepository;
import developer.ezandro.forumhubapi.repository.TopicRepository;
import developer.ezandro.forumhubapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TopicService {
    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public TopicResponseDTO createTopic(TopicRequestDTO dto, String username) {
        User author = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Course course = this.courseRepository.findById(dto.courseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        Topic topic = new Topic(
                null,
                dto.title(),
                dto.message(),
                LocalDateTime.now(),
                TopicStatus.OPEN,
                author,
                course,
                null
        );

        topic = this.topicRepository.save(topic);

        return this.toDTO(topic);
    }

    public TopicResponseDTO toDTO(Topic topic) {
        return new TopicResponseDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getCreationDate(),
                topic.getStatus().name(),
                topic.getAuthor().getName(),
                topic.getCourse().getName()
        );
    }
}