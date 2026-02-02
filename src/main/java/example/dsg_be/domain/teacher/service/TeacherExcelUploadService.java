package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherExcelUploadService {

    private final TeacherRepository teacherRepository;

    public void execute(MultipartFile file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream()); // 액셀 파일 열기
            XSSFSheet sheet = workbook.getSheetAt(0); // 시트 불러오기

            List<TeacherEntity> teachers = new ArrayList<>(); // 데이터들을 담을 List

            // 헤더 0인덱스 제외 값을 추출하기
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue; // 행이 완전히 비었을 경우 처리해줌

                Long number = (long) i;
                String department = row.getCell(1).getStringCellValue();
                String position = row.getCell(2).getStringCellValue();
                String name = row.getCell(3).getStringCellValue();

                teachers.add(TeacherEntity.builder()
                        .number(number)
                        .department(department)
                        .position(position)
                        .name(name)
                        .createdAt(LocalDateTime.now())
                        .build());
            }
            teacherRepository.saveAll(teachers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // 행이 비어있는 셀이 있는지 확인해서 위에서 액셀 행에 따라 처리하도록 하는 메서드
    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false; // 하나의 셀이라도 값이 있다면
        }
        return true;
    }
}
