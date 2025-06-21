package developer.ezandro.forumhubapi.controller;

import developer.ezandro.forumhubapi.dto.TopicRequestDTO;
import developer.ezandro.forumhubapi.dto.TopicResponseDTO;
import developer.ezandro.forumhubapi.dto.TopicUpdateRequestDTO;
import developer.ezandro.forumhubapi.infra.security.TokenService;
import developer.ezandro.forumhubapi.repository.UserRepository;
import developer.ezandro.forumhubapi.service.TopicService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@ActiveProfiles(value = "test")
@WebMvcTest(
        value = TopicController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = developer.ezandro.forumhubapi.infra.security.SecurityFilter.class
        )
)
class TopicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public TopicService topicService() {
            return Mockito.mock(TopicService.class);
        }

        @Bean
        public TokenService tokenService() {
            return Mockito.mock(TokenService.class);
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @Autowired
    private TopicService topicService;

    private TopicResponseDTO buildTopicDTO() {
        return new TopicResponseDTO(
                1L,
                "Test Topic",
                "Test Message",
                LocalDateTime.of(2024, 1, 1, 10, 0),
                "OPEN",
                "Test Author",
                "Test Course"
        );
    }

    @Test
    @DisplayName(value = "Should return 200 and a paged list of topics")
    void testListTopics() throws Exception {
        var topic = this.buildTopicDTO();
        var page = new PageImpl<>(List.of(topic), PageRequest.of(0, 10), 1);
        when(this.topicService.listTopics(any())).thenReturn(page);

        this.mockMvc.perform(get("/topics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Test Topic"))
                .andExpect(jsonPath("$.content[0].message").value("Test Message"))
                .andExpect(jsonPath("$.content[0].creationDate").exists())
                .andExpect(jsonPath("$.content[0].status").value("OPEN"))
                .andExpect(jsonPath("$.content[0].authorName").value("Test Author"))
                .andExpect(jsonPath("$.content[0].courseName").value("Test Course"));
    }

    @Test
    @DisplayName(value = "Should return 200 and a topic by id")
    void testGetTopicById() throws Exception {
        var topic = this.buildTopicDTO();
        when(this.topicService.getTopicDetails(1L)).thenReturn(topic);

        this.mockMvc.perform(get("/topics/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Topic"))
                .andExpect(jsonPath("$.message").value("Test Message"))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.authorName").value("Test Author"))
                .andExpect(jsonPath("$.courseName").value("Test Course"));
    }

    @Test
    @DisplayName(value = "Should return 201 when creating a topic")
    void testCreateTopic() throws Exception {
        var response = this.buildTopicDTO();
        when(this.topicService.createTopic(any(TopicRequestDTO.class), anyString())).thenReturn(response);

        this.mockMvc.perform(post("/topics")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Test Topic",
                                    "message": "Test Message",
                                    "courseId": 1
                                }
                                """)
                        .header("Authorization", "Bearer fake-jwt"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Topic"));
    }

    @Test
    @DisplayName(value = "Should return 200 when updating a topic")
    void testUpdateTopic() throws Exception {
        var response = new TopicResponseDTO(
                1L,
                "Updated Title",
                "Updated Message",
                LocalDateTime.of(2024, 1, 2, 10, 0),
                "CLOSED",
                "Test Author",
                "Test Course"
        );
        when(this.topicService.updateTopic(eq(1L), any(TopicUpdateRequestDTO.class))).thenReturn(response);

        this.mockMvc.perform(put("/topics/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Updated Title",
                                    "message": "Updated Message",
                                    "status": "CLOSED"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.message").value("Updated Message"))
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    @DisplayName(value = "Should return 204 when deleting a topic")
    void testDeleteTopic() throws Exception {
        doNothing().when(this.topicService).deleteById(1L);

        this.mockMvc.perform(delete("/topics/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}