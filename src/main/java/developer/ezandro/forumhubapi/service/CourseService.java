package developer.ezandro.forumhubapi.service;

import developer.ezandro.forumhubapi.dto.CourseRequestDTO;
import developer.ezandro.forumhubapi.dto.CourseResponseDTO;
import developer.ezandro.forumhubapi.exception.UserNotFoundException;
import developer.ezandro.forumhubapi.model.Course;
import developer.ezandro.forumhubapi.model.User;
import developer.ezandro.forumhubapi.repository.CourseRepository;
import developer.ezandro.forumhubapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    public CourseResponseDTO create(CourseRequestDTO dto, String username) {
        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        Course course = new Course(null, dto.name(), dto.category(), user);
        Course saved = this.courseRepository.save(course);
        return new CourseResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getCategory(),
                saved.getUser().getId(),
                saved.getUser().getName(),
                saved.getUser().getEmail()
        );
    }
}