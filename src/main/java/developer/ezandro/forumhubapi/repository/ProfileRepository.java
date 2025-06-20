package developer.ezandro.forumhubapi.repository;

import developer.ezandro.forumhubapi.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}