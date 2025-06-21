package developer.ezandro.forumhubapi.repository;

import developer.ezandro.forumhubapi.model.Profile;
import developer.ezandro.forumhubapi.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @DisplayName(value = "findByEmail should return the existing user")
    void findByEmailReturnsUser() {
        Profile profile = new Profile(null, "ROLE_USER");
        this.profileRepository.save(profile);

        Set<Profile> profiles = new HashSet<>();
        profiles.add(profile);

        User user = new User(null, "John Doe", "john@example.com", "password", profiles);
        this.userRepository.save(user);

        Optional<User> result = this.userRepository.findByEmail("john@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName(value = "findByEmail should return empty for a non-existent email")
    void findByEmailReturnsEmpty() {
        Optional<User> result = this.userRepository.findByEmail("noone@example.com");
        assertThat(result).isEmpty();
    }
}