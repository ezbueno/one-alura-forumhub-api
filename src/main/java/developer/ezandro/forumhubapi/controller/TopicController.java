package developer.ezandro.forumhubapi.controller;

import developer.ezandro.forumhubapi.dto.TopicRequestDTO;
import developer.ezandro.forumhubapi.dto.TopicResponseDTO;
import developer.ezandro.forumhubapi.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/topics")
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<TopicResponseDTO> createTopic(
            @RequestBody @Valid TopicRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        TopicResponseDTO topicResponseDTO = this.topicService.createTopic(dto, userDetails.getUsername());
        return ResponseEntity.status(201).body(topicResponseDTO);
    }
}
