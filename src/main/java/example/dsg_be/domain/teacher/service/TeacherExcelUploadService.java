package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.presentation.dto.ExcelReadData;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import example.dsg_be.global.util.ExcelDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherExcelUploadService {
    private final TeacherRepository teacherRepository;

    @Transactional
    public void execute(MultipartFile file) {
        List<ExcelReadData> dataList = ExcelDataUtil.readExcel(file);
        log.info("읽은 데이터 수: {}", dataList.size());
        for (ExcelReadData data : dataList) {
            log.info("읽은 데이터 - name: {}, department: {}, position: {}",
                    data.getName(), data.getDepartment(), data.getPosition());
        }
        saveTeacher(dataList);
    }

    public void saveTeacher(List<ExcelReadData> dataList) {
        List<TeacherEntity> teachers = new ArrayList<>(); // 데이터들을 담을 List

        Long number = 1L;
        for (int i = 0; i < dataList.size(); i++) {
            ExcelReadData data = dataList.get(i);
            String name = data.getName();
            String department = data.getDepartment();
            String position = data.getPosition();

            teachers.add(TeacherEntity.builder()
                    .number(number)
                    .department(department)
                    .position(position)
                    .name(name)
                    .build());
            number++;
        }
        teacherRepository.saveAll(teachers);
    }
}
