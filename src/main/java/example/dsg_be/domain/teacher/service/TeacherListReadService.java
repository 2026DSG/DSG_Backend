package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import example.dsg_be.domain.teacher.presentation.dto.response.TeacherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherListReadService {
    private final TeacherRepository teacherRepository;

    public List<TeacherResponse> execute() {
        List<TeacherEntity> teachers = teacherRepository.findAll();

        return teachers.stream()
                .map(t -> TeacherResponse.builder()
                        .id(t.getTeacherId())
                        .number(t.getNumber())
                        .department(t.getDepartment())
                        .position(t.getPosition())
                        .name(t.getName())
                        .createdAt(t.getCreatedAt())
                        .build())
                .toList(); // 변환된 결과들을 다시 리스트로 만듭니다.
    }
}
