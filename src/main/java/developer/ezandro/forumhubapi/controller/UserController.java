package developer.ezandro.forumhubapi.controller;

import developer.ezandro.forumhubapi.dto.UserRequestDTO;
import developer.ezandro.forumhubapi.dto.UserResponseDTO;
import developer.ezandro.forumhubapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO userRequestDTO,
                                                  UriComponentsBuilder uriComponentsBuilder) {
        UserResponseDTO userResponseDTO = this.userService.create(userRequestDTO);
        URI uri = uriComponentsBuilder
                .path("/users/{id}")
                .buildAndExpand(userResponseDTO.id())
                .toUri();
        return ResponseEntity.created(uri).body(userResponseDTO);
    }
}