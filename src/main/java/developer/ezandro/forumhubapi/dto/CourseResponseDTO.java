package developer.ezandro.forumhubapi.dto;

public record CourseResponseDTO(
        Long id,
        String name,
        String category,
        Long userId,
        String userName,
        String userEmail) {
}