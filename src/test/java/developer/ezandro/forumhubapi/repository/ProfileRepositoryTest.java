package developer.ezandro.forumhubapi.repository;

import developer.ezandro.forumhubapi.model.Profile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @DisplayName(value = "findByName should return the existing profile")
    void findByNameReturnsProfile() {
        Profile profile = new Profile(null, "ROLE_USER");
        this.profileRepository.save(profile);

        Optional<Profile> result = this.profileRepository.findByName("ROLE_USER");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName(value = "findByName should return empty for a non-existent name")
    void findByNameReturnsEmpty() {
        Optional<Profile> result = this.profileRepository.findByName("ROLE_UNKNOWN");
        assertThat(result).isEmpty();
    }
}