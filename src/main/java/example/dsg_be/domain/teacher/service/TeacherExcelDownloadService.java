package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TeacherExcelDownloadService { // TODO : 액셀 셀 중앙 정렬하기, 헤더 스타일링 하기.
    private final TeacherRepository teacherRepository;

    public byte[] execute() throws IOException {
        if (teacherRepository.findAll().isEmpty()) {
            return null;
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("교직원 목록"); // 새 시트 생성

        Row headerRow = sheet.createRow(0); // 헤더 행
        headerRow.setHeightInPoints(17.4f); // 셀 높이 설정
        String[] headers = {"순번", "부서", "직위", "이름"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        sheet.setColumnWidth(0, (int) (8.3 * 256)); // 셀 너비 설정
        sheet.setColumnWidth(1, (int) (13.8 * 256));
        sheet.setColumnWidth(2, (int) (9.7 * 256));
        sheet.setColumnWidth(3, (int) (13.4 * 256));

        int rowNum = 1;
        for(TeacherEntity teacher : teacherRepository.findAll()) {
            Row dataRow = sheet.createRow(rowNum++);
            dataRow.setHeightInPoints(17.4f);

            dataRow.createCell(0).setCellValue(rowNum); // TODO: 넘버링 처리
            dataRow.createCell(1).setCellValue(teacher.getDepartment());
            dataRow.createCell(2).setCellValue(teacher.getPosition());
            dataRow.createCell(3).setCellValue(teacher.getName());
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}
