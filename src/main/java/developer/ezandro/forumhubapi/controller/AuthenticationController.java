package developer.ezandro.forumhubapi.controller;

import developer.ezandro.forumhubapi.dto.AuthenticationRequestDTO;
import developer.ezandro.forumhubapi.dto.AuthenticationResponseDTO;
import developer.ezandro.forumhubapi.infra.security.TokenService;
import developer.ezandro.forumhubapi.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody @Valid AuthenticationRequestDTO authRequest) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password());

        Authentication authentication = this.authenticationManager.authenticate(authToken);

        User user = (User) authentication.getPrincipal();
        String token = this.tokenService.generateJwtToken(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthenticationResponseDTO(token));
    }
}