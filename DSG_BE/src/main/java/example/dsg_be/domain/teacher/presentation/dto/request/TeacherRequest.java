package example.dsg_be.domain.teacher.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeacherRequest {

    private String department;

    private String position;

    private String name;

}