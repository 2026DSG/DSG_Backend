package example.dsg_be.domain.teacher.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeacherRequest { // AddService만 적용되는 DTO
    @NotBlank(message = "부서는 비어있을 수 없습니다")
    @Size(min = 2, max = 50, message = "2 ~ 50자 범위만 허용됩니다")
    private String department;

    @NotBlank(message = "직위는 비어있을 수 없습니다")
    @Size(min = 2, max = 50, message = "2 ~ 50자 범위만 허용됩니다")
    private String position;

    @NotBlank(message = "이름은 비어있을 수 없습니다")
    @Size(min = 2, max = 4, message = "2 ~ 4자 범위만 허용됩니다")
    private String name;
}
