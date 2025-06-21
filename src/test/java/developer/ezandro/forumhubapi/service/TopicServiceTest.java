package developer.ezandro.forumhubapi.service;

import developer.ezandro.forumhubapi.dto.TopicRequestDTO;
import developer.ezandro.forumhubapi.dto.TopicResponseDTO;
import developer.ezandro.forumhubapi.dto.TopicUpdateRequestDTO;
import developer.ezandro.forumhubapi.exception.CourseNotFoundException;
import developer.ezandro.forumhubapi.exception.TopicNotFoundException;
import developer.ezandro.forumhubapi.exception.UserNotFoundException;
import developer.ezandro.forumhubapi.model.*;
import developer.ezandro.forumhubapi.repository.CourseRepository;
import developer.ezandro.forumhubapi.repository.TopicRepository;
import developer.ezandro.forumhubapi.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TopicServiceTest {
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TopicService topicService;

    private User user;
    private Course course;
    private Topic topic;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        Set<Profile> profiles = new HashSet<>();
        user = new User(
                1L,
                "User",
                "user@email.com",
                "password",
                profiles
        );

        course = new Course(
                1L,
                "Course",
                "Category",
                user
        );

        topic = new Topic(
                1L,
                "Title",
                "Message",
                LocalDateTime.of(2024, 6, 21, 10, 0),
                TopicStatus.OPEN,
                user,
                course,
                Collections.emptyList()
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName(value = "createTopic - success")
    void shouldCreateTopic() {
        var dto = new TopicRequestDTO("Title", "Message", 1L);
        when(this.userRepository.findByEmail("user@email.com")).thenReturn(Optional.of(user));
        when(this.courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(this.topicRepository.save(any(Topic.class))).thenAnswer(invocation -> {
            Topic t = invocation.getArgument(0, Topic.class);
            t = new Topic(
                    1L, t.getTitle(), t.getMessage(), t.getCreationDate(),
                    t.getStatus(), t.getAuthor(), t.getCourse(), Collections.emptyList()
            );
            return t;
        });

        TopicResponseDTO result = this.topicService.createTopic(dto, "user@email.com");

        assertThat(result.title()).isEqualTo("Title");
        assertThat(result.authorName()).isEqualTo("User");
        assertThat(result.courseName()).isEqualTo("Course");
        verify(this.topicRepository).save(any(Topic.class));
    }

    @Test
    @DisplayName(value = "createTopic - user not found")
    void shouldThrowWhenUserNotFound() {
        var dto = new TopicRequestDTO("Title", "Message", 1L);
        when(this.userRepository.findByEmail("user@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.topicService.createTopic(dto, "user@email.com"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName(value = "createTopic - course not found")
    void shouldThrowWhenCourseNotFound() {
        var dto = new TopicRequestDTO("Title", "Message", 2L);
        when(this.userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(this.courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.topicService.createTopic(dto, "user@email.com"))
                .isInstanceOf(CourseNotFoundException.class)
                .hasMessageContaining("Course not found");
    }

    @Test
    @DisplayName(value = "listTopics - should return paginated topics")
    void shouldListTopics() {
        var pageable = PageRequest.of(0, 10);
        when(this.topicRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(topic)));

        var result = this.topicService.listTopics(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().title()).isEqualTo("Title");
    }

    @Test
    @DisplayName(value = "getTopicDetails - success")
    void shouldGetTopicDetails() {
        when(this.topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        var result = this.topicService.getTopicDetails(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Title");
    }

    @Test
    @DisplayName(value = "getTopicDetails - not found")
    void shouldThrowWhenTopicNotFound() {
        when(this.topicRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.topicService.getTopicDetails(1L))
                .isInstanceOf(TopicNotFoundException.class)
                .hasMessageContaining("Topic not found");
    }

    @Test
    @DisplayName(value = "updateTopic - success")
    void shouldUpdateTopic() {
        when(this.topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        var dto = new TopicUpdateRequestDTO("New Title", "New Msg", TopicStatus.CLOSED);

        var result = this.topicService.updateTopic(1L, dto);

        assertThat(result.title()).isEqualTo("New Title");
        assertThat(result.message()).isEqualTo("New Msg");
        assertThat(result.status()).isEqualTo("CLOSED");
    }

    @Test
    @DisplayName(value = "updateTopic - not found")
    void shouldThrowWhenUpdateTopicNotFound() {
        when(this.topicRepository.findById(1L)).thenReturn(Optional.empty());
        var dto = new TopicUpdateRequestDTO("T", "M", TopicStatus.OPEN);

        assertThatThrownBy(() -> this.topicService.updateTopic(1L, dto))
                .isInstanceOf(TopicNotFoundException.class)
                .hasMessageContaining("Topic not found");
    }

    @Test
    @DisplayName(value = "deleteById - success")
    void shouldDeleteTopic() {
        when(this.topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        doNothing().when(this.topicRepository).deleteById(1L);

        this.topicService.deleteById(1L);

        verify(this.topicRepository).deleteById(1L);
    }

    @Test
    @DisplayName(value = "deleteById - not found")
    void shouldThrowWhenDeleteTopicNotFound() {
        when(this.topicRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.topicService.deleteById(1L))
                .isInstanceOf(TopicNotFoundException.class)
                .hasMessageContaining("Topic not found");
    }
}