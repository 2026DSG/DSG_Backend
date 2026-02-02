package example.dsg_be.domain.teacher.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TeacherResponse {

    private Long id;

    private Long number;

    private String department;

    private String position;

    private LocalDateTime createdAt;

    private String name;

}
