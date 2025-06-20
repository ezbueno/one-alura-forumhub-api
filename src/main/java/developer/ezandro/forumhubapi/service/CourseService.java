package developer.ezandro.forumhubapi.service;

import developer.ezandro.forumhubapi.dto.CourseRequestDTO;
import developer.ezandro.forumhubapi.dto.CourseResponseDTO;
import developer.ezandro.forumhubapi.model.Course;
import developer.ezandro.forumhubapi.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Transactional
    public CourseResponseDTO create(CourseRequestDTO dto) {
        Course course = new Course(null, dto.name(), dto.category());
        Course saved = this.courseRepository.save(course);
        return new CourseResponseDTO(saved.getId(), saved.getName(), saved.getCategory());
    }
}