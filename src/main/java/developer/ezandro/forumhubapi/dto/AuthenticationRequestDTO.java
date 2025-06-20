package developer.ezandro.forumhubapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequestDTO(
        @NotBlank(message = "E-mail is required")
        @Email(message = "Invalid e-mail format") String email,
        @NotBlank(message = "Password is required") String password) {
}