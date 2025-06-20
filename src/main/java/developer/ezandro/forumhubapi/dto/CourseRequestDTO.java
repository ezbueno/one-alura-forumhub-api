package developer.ezandro.forumhubapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CourseRequestDTO(
        @NotBlank(message = "Course name is required") String name,
        @NotBlank(message = "Course category is required") String category) {
}