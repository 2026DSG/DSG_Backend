package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherDeleteService {
    private final TeacherRepository teacherRepository;

    public void execute(Long id) {
        teacherRepository.deleteById(id);
    }
}
