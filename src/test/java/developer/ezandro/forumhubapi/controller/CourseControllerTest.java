package developer.ezandro.forumhubapi.controller;

import developer.ezandro.forumhubapi.dto.CourseRequestDTO;
import developer.ezandro.forumhubapi.dto.CourseResponseDTO;
import developer.ezandro.forumhubapi.service.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@ActiveProfiles(value = "test")
@WebMvcTest(
        value = CourseController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = developer.ezandro.forumhubapi.infra.security.SecurityFilter.class
        )
)
class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @Test
    void createCourse_success() throws Exception {
        CourseResponseDTO responseDTO = new CourseResponseDTO(1L, "Java", "Programming", 2L, "User", "user@email.com");
        Mockito.when(this.courseService.create(any(CourseRequestDTO.class), anyString())).thenReturn(responseDTO);

        this.mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Java\",\"category\":\"Programming\"}")
                        .principal(() -> "user@email.com")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/courses/1")))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java"));
    }

    @Test
    void createCourse_invalidPayload() throws Exception {
        this.mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"category\":\"\"}")
                        .principal(() -> "user@email.com")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}