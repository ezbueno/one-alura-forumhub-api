package developer.ezandro.forumhubapi.repository;

import developer.ezandro.forumhubapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByName(String name);

    Optional<User> findByEmail(String username);
}