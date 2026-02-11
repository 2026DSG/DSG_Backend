package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.exception.TeacherNotFoundException;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherDeleteService {
    private final TeacherRepository teacherRepository;

    @Transactional
    public void execute(Long id) {
        TeacherEntity teacher = teacherRepository.findById(id)
                .orElseThrow(()-> TeacherNotFoundException.EXCEPTION);
        teacher.changeStatus(); // isRemoved 속성 값 변경
    }
}
