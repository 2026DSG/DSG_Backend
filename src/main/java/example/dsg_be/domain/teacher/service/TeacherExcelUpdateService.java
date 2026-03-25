package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.presentation.dto.ExcelReadData;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import example.dsg_be.global.util.ExcelDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherExcelUpdateService {
    private final TeacherRepository teacherRepository;

    public void execute(MultipartFile file) {
        List<ExcelReadData> dataList = ExcelDataUtil.readExcel(file);
        updateTeacher(dataList);
    }

    @Transactional
    public void updateTeacher(List<ExcelReadData> dataList) {
        List<TeacherEntity> allTeachers = teacherRepository.findAll();
            Map<String, TeacherEntity> teacherMap = allTeachers.stream()
                    .collect(Collectors.toMap(TeacherEntity::getName, teacherEntity -> teacherEntity));
            // DB에 있는 모든 교직원을 <이름, 엔티티>Map으로

            List<TeacherEntity> teachersData = new ArrayList<>(); // 데이터들 담을 List

            Long num = 1L;
            for (int i = 0; i < dataList.size(); i++) {
                ExcelReadData data = dataList.get(i);
                String name = data.getName();
                String department = data.getDepartment();
                String position = data.getPosition();

                if (teacherMap.containsKey(name)) { // DB에 이미 있는 교직원일 경우
                    TeacherEntity teacher = teacherMap.get(name);
                    teacher.update(department, position, num);

                    teacherMap.remove(name); // 식별 Map에선 제외
                } else { // DB에 없는 선생님일 경우
                    TeacherEntity newTeacher = TeacherEntity.builder()
                            .number(num)
                            .name(name)
                            .department(department)
                            .position(position)
                            .build();
                    teachersData.add(newTeacher); // 전체 데이터에 담기
                }
                num++;
            }
            for (TeacherEntity leftTeacher : teacherMap.values()) { // Map에 남아있는 교직원 = 액셀에서 제외된 값
                leftTeacher.changeStatus(); // isRemoved 속성을 바꿈
                teachersData.add(leftTeacher); // 전체 데이터에 담기
            }
            teacherRepository.saveAll(teachersData);
    }
}
