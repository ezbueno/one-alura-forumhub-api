package developer.ezandro.forumhubapi.service;

import developer.ezandro.forumhubapi.dto.CourseRequestDTO;
import developer.ezandro.forumhubapi.dto.CourseResponseDTO;
import developer.ezandro.forumhubapi.exception.UserNotFoundException;
import developer.ezandro.forumhubapi.model.Course;
import developer.ezandro.forumhubapi.model.User;
import developer.ezandro.forumhubapi.repository.CourseRepository;
import developer.ezandro.forumhubapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        this.courseRepository = mock(CourseRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.courseService = new CourseService(this.courseRepository, this.userRepository);
    }

    @Test
    void create_success() {
        CourseRequestDTO dto = new CourseRequestDTO("Java", "Programming");
        User user = new User(2L, "User", "user@email.com", "pass", null);
        Course savedCourse = new Course(1L, "Java", "Programming", user);

        when(this.userRepository.findByEmail("user@email.com")).thenReturn(Optional.of(user));
        when(this.courseRepository.save(ArgumentMatchers.any(Course.class))).thenReturn(savedCourse);

        CourseResponseDTO result = this.courseService.create(dto, "user@email.com");
        assertNotNull(result);
        assertEquals("Java", result.name());
        assertEquals(2L, result.userId());
    }

    @Test
    void create_userNotFound() {
        CourseRequestDTO dto = new CourseRequestDTO("Java", "Programming");
        when(this.userRepository.findByEmail("user@email.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> this.courseService.create(dto, "user@email.com"));
        verify(this.courseRepository, never()).save(any());
    }
}