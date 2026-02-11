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

@Service
@RequiredArgsConstructor
public class TeacherExcelUploadService {
    private final TeacherRepository teacherRepository;

    @Transactional
    public void execute(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".xlsx")) {
            throw NotExcelFileException.Exception;
        }

        try(XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) { // 액셀 파일 열기 (try-with-resource)
            XSSFSheet sheet = workbook.getSheetAt(0); // 시트 불러오기

            List<TeacherEntity> teachers = new ArrayList<>(); // 데이터들을 담을 List

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || ExcelUtil.isRowEmpty(row)) {
                    continue;
                }

                String name = row.getCell(3).getStringCellValue().trim();
                if (name.isEmpty()) {
                    throw NameCellIsEmptyException.EXCEPTION;
                }
                Long number = (long) i;
                String department = row.getCell(1).getStringCellValue();
                String position = row.getCell(2).getStringCellValue();

                teachers.add(TeacherEntity.builder()
                        .number(number)
                        .department(department)
                        .position(position)
                        .name(name)
                        .build());
            }
            teacherRepository.saveAll(teachers);
        }catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw NotExcelFileException.Exception;
        }
    }
}
