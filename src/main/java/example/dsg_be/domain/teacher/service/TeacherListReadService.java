package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import example.dsg_be.domain.teacher.presentation.dto.response.TeacherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherListReadService {
    private final TeacherRepository teacherRepository;

    @Transactional(readOnly = true)
    public List<TeacherResponse> execute() {
        List<TeacherEntity> teachers = teacherRepository.findAllByIsRemovedFalse();

        return teachers.stream()
                .map(TeacherResponse::new).toList(); // new : 생성자 호출
    }
}
