package example.dsg_be.domain.teacher.service;

import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.exception.TeacherNotExistException;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import example.dsg_be.global.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherExcelDownloadService {
    private final TeacherRepository teacherRepository;

    @Transactional
    public byte[] execute() throws IOException {
        List<TeacherEntity> teachers = teacherRepository.findAllByIsRemovedFalse();
        if (teachers.isEmpty()) {
            throw TeacherNotExistException.EXCEPTION;
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("교직원 목록");

        CellStyle headerStyle = ExcelUtil.getHeaderStyle(workbook);
        CellStyle bodyStyle = ExcelUtil.getBodyStyle(workbook);

        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(17.4f);
        String[] headers = {"순번", "부서", "직위", "이름"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle); // 헤더 스타일 적용
        }

        sheet.setColumnWidth(0, (int) (8.3 * 256));
        sheet.setColumnWidth(1, (int) (13.8 * 256));
        sheet.setColumnWidth(2, (int) (9.7 * 256));
        sheet.setColumnWidth(3, (int) (13.4 * 256));

        int rowNum = 1;
        for(TeacherEntity teacher : teachers) {
            Row dataRow = sheet.createRow(rowNum++);
            dataRow.setHeightInPoints(17.4f);

            // 셀 생성 시 스타일 함께 적용 (헬퍼 메서드 사용)
            ExcelUtil.createCellWithStyle(dataRow, 0, String.valueOf(rowNum - 1), bodyStyle);
            ExcelUtil.createCellWithStyle(dataRow, 1, teacher.getDepartment(), bodyStyle);
            ExcelUtil.createCellWithStyle(dataRow, 2, teacher.getPosition(), bodyStyle);
            ExcelUtil.createCellWithStyle(dataRow, 3, teacher.getName(), bodyStyle);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}
