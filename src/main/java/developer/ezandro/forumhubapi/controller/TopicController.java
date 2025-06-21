package developer.ezandro.forumhubapi.controller;

import developer.ezandro.forumhubapi.dto.TopicRequestDTO;
import developer.ezandro.forumhubapi.dto.TopicResponseDTO;
import developer.ezandro.forumhubapi.dto.TopicUpdateRequestDTO;
import developer.ezandro.forumhubapi.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/topics")
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<TopicResponseDTO> createTopic(
            @RequestBody @Valid TopicRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        TopicResponseDTO topicResponseDTO = this.topicService.createTopic(dto, userDetails.getUsername());
        return ResponseEntity.status(201).body(topicResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<TopicResponseDTO>> listTopics(
            @PageableDefault(size = 10,
                    sort = "creationDate",
                    direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.topicService.listTopics(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TopicResponseDTO> getTopicDetails(
            @PathVariable(value = "id") Long id
    ) {
        TopicResponseDTO topic = this.topicService.getTopicDetails(id);
        return ResponseEntity.ok(topic);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TopicResponseDTO> updateTopic(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid TopicUpdateRequestDTO updateDTO
    ) {
        TopicResponseDTO updated = this.topicService.updateTopic(id, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable(value = "id") Long id) {
        this.topicService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
