package developer.ezandro.forumhubapi.service;

import developer.ezandro.forumhubapi.model.User;
import developer.ezandro.forumhubapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {
    private UserRepository userRepository;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(UserRepository.class);
        this.customUserDetailsService = new CustomUserDetailsService(this.userRepository);
    }

    @Test
    void loadUserByUsername_success() {
        User user = new User(1L, "John", "john@email.com", "pass", null);
        when(this.userRepository.findByEmail("john@email.com")).thenReturn(Optional.of(user));

        var result = this.customUserDetailsService.loadUserByUsername("john@email.com");
        assertNotNull(result);
        assertEquals("john@email.com", result.getUsername());
    }

    @Test
    void loadUserByUsername_userNotFound() {
        when(this.userRepository.findByEmail("john@email.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> this.customUserDetailsService.loadUserByUsername("john@email.com"));
    }
}