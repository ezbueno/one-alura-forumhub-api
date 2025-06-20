package developer.ezandro.forumhubapi.repository;

import developer.ezandro.forumhubapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}