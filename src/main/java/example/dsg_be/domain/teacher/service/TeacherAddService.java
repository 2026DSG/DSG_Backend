package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.exception.NameAlreadyExistException;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import example.dsg_be.domain.teacher.presentation.dto.request.TeacherRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherAddService {
    private final TeacherRepository teacherRepository;

    @Transactional
    public void execute(TeacherRequest teacherRequest) {
        Optional<TeacherEntity> teacherOptional = teacherRepository.findByName(teacherRequest.getName());
        if (teacherOptional.isPresent()) {
            TeacherEntity teacher = teacherOptional.get();
            if (!teacher.getIsRemoved()) {
                throw NameAlreadyExistException.EXCEPTION;
            } else {
                Long maxNum = teacherRepository.findMaxNumberByIsRemovedFalse().orElse(0L);

                teacher.update(
                        teacherRequest.getDepartment(),
                        teacherRequest.getPosition(),
                        maxNum + 1
                );

                teacherRepository.save(teacher);
            }
        } else {
            Long maxNum = teacherRepository.findMaxNumberByIsRemovedFalse().orElse(0L);

            TeacherEntity newTeacher = TeacherEntity.builder()
                    .department(teacherRequest.getDepartment())
                    .position(teacherRequest.getPosition())
                    .name(teacherRequest.getName())
                    .number(maxNum + 1)
                    .build();

            teacherRepository.save(newTeacher);

        }
    }
}
