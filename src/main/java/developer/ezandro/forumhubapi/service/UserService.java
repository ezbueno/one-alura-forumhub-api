package developer.ezandro.forumhubapi.service;

import developer.ezandro.forumhubapi.dto.UserRequestDTO;
import developer.ezandro.forumhubapi.dto.UserResponseDTO;
import developer.ezandro.forumhubapi.exception.ProfileNotFoundException;
import developer.ezandro.forumhubapi.model.Profile;
import developer.ezandro.forumhubapi.model.User;
import developer.ezandro.forumhubapi.repository.ProfileRepository;
import developer.ezandro.forumhubapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        Profile userProfile = this.profileRepository
                .findByName("USER").orElseThrow(
                        () -> new ProfileNotFoundException("Default profile USER not found")
                );

        Set<Profile> profiles = new HashSet<>();
        profiles.add(userProfile);

        String hashedPassword = this.passwordEncoder.encode(dto.password());

        User user = new User(null, dto.name(), dto.email(), hashedPassword, profiles);
        this.userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }
}