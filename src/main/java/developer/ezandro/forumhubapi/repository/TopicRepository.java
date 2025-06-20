package developer.ezandro.forumhubapi.repository;

import developer.ezandro.forumhubapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}