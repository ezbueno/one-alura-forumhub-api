package developer.ezandro.forumhubapi.controller;

import developer.ezandro.forumhubapi.dto.CourseRequestDTO;
import developer.ezandro.forumhubapi.dto.CourseResponseDTO;
import developer.ezandro.forumhubapi.dto.UserResponseDTO;
import developer.ezandro.forumhubapi.service.CourseService;
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
@RequestMapping(value = "/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@RequestBody @Valid CourseRequestDTO courseRequestDTO,
                                                  UriComponentsBuilder uriComponentsBuilder) {
        CourseResponseDTO courseResponseDTO = this.courseService.create(courseRequestDTO);
        URI uri = uriComponentsBuilder
                .path("/courses/{id}")
                .buildAndExpand(courseResponseDTO.id())
                .toUri();
        return ResponseEntity.created(uri).body(courseResponseDTO);
    }
}