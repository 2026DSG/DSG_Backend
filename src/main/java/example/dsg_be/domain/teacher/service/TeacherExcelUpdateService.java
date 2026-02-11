package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.exception.NameCellIsEmptyException;
import example.dsg_be.domain.teacher.exception.NotExcelFileException;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    @Transactional
    public void execute(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".xlsx")) {
            throw NotExcelFileException.Exception;
        }

        try(XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0); // 시트 불러오기

            List<TeacherEntity> allTeachers = teacherRepository.findAll();
            Map<String, TeacherEntity> teacherMap = allTeachers.stream()
                    .collect(Collectors.toMap(TeacherEntity::getName, teacherEntity -> teacherEntity));
            // DB에 있는 모든 교직원을 <이름, 엔티티>Map으로

            List<TeacherEntity> teachersData = new ArrayList<>(); // 데이터들 담을 List

            Long num = 1L;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 헤더 행 제외
                Row row = sheet.getRow(i);
                if (row == null || ExcelUtil.isRowEmpty(row)) {
                    continue;
                }

                String name = row.getCell(3).getStringCellValue().trim();
                if (name.isEmpty()) {
                    throw NameCellIsEmptyException.EXCEPTION;
                }
                String department = row.getCell(1).getStringCellValue();
                String position = row.getCell(2).getStringCellValue();

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
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw NotExcelFileException.Exception;
        }
    }
}
