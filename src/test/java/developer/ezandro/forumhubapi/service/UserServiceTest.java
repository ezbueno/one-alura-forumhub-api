package developer.ezandro.forumhubapi.service;

import developer.ezandro.forumhubapi.dto.UserRequestDTO;
import developer.ezandro.forumhubapi.dto.UserResponseDTO;
import developer.ezandro.forumhubapi.exception.ProfileNotFoundException;
import developer.ezandro.forumhubapi.model.Profile;
import developer.ezandro.forumhubapi.model.User;
import developer.ezandro.forumhubapi.repository.ProfileRepository;
import developer.ezandro.forumhubapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(UserRepository.class);
        this.profileRepository = mock(ProfileRepository.class);
        this.passwordEncoder = mock(PasswordEncoder.class);
        this.userService = new UserService(this.userRepository, this.profileRepository, this.passwordEncoder);
    }

    @Test
    void create_success() {
        UserRequestDTO dto = new UserRequestDTO("John", "john@email.com", "123456");
        Profile profile = new Profile(1L, "USER");
        when(this.profileRepository.findByName("USER")).thenReturn(Optional.of(profile));
        when(this.passwordEncoder.encode("123456")).thenReturn("hashed");
        when(this.userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return new User(1L, user.getName(), user.getEmail(), user.getPassword(), user.getProfiles());
        });

        UserResponseDTO result = this.userService.create(dto);

        assertNotNull(result);
        assertEquals("John", result.name());
        assertEquals("john@email.com", result.email());
        assertEquals(1L, result.id());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(this.userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("hashed", savedUser.getPassword());
        assertTrue(savedUser.getProfiles().stream().anyMatch(p -> "USER".equals(p.getName())));
    }

    @Test
    void create_profileNotFound() {
        UserRequestDTO dto = new UserRequestDTO("John", "john@email.com", "123456");
        when(this.profileRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> this.userService.create(dto));
        verify(this.userRepository, never()).save(any());
    }
}