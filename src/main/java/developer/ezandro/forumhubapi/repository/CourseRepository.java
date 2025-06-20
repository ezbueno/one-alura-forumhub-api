package developer.ezandro.forumhubapi.repository;

import developer.ezandro.forumhubapi.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}