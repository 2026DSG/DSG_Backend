package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import example.dsg_be.domain.teacher.presentation.dto.request.TeacherRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TeacherAddService {
    private final TeacherRepository teacherRepository;

    public void execute(TeacherRequest teacherRequest) {
        TeacherEntity teacher = TeacherEntity.builder()
                .department(teacherRequest.getDepartment())
                .position(teacherRequest.getPosition())
                .name(teacherRequest.getName())
                .createdAt(LocalDateTime.now())
                .build();

        teacherRepository.save(teacher);
    }
}
