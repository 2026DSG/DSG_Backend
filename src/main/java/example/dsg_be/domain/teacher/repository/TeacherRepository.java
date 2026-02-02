package example.dsg_be.domain.teacher.repository;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Long> {
}
