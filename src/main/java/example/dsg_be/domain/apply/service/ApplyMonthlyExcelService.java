package example.dsg_be.domain.apply.service;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.apply.exception.ApplyNotFoundException;
import example.dsg_be.domain.apply.repository.ApplyRepository;
import example.dsg_be.global.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplyMonthlyExcelService {

    private final ApplyRepository applyRepository;

    @Transactional(readOnly = true)
    public byte[] execute(int year, int month) throws IOException {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<ApplyEntity> applyList = applyRepository
                .findAllByCreatedAtBetweenOrderByCreatedAtAsc(start, end);

        if (applyList.isEmpty()) {
            throw ApplyNotFoundException.EXCEPTION;
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("월별 개인별 목록");

            CellStyle headerStyle = ExcelUtil.getHeaderStyle(workbook);
            CellStyle bodyStyle = ExcelUtil.getBodyStyle(workbook);

            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("월별 개인별 목록");

            Row dateRow = sheet.createRow(2);
            dateRow.createCell(0).setCellValue(year + "-" + month + "월");

            Row headerRow = sheet.createRow(3);
            headerRow.setHeightInPoints(17.4f);
            String[] headers = {"부서", "이름", "일자", "초과근무", "개인부담"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            sheet.setColumnWidth(0, (int) (13.8 * 256));
            sheet.setColumnWidth(1, (int) (13.4 * 256));
            sheet.setColumnWidth(2, (int) (15.0 * 256));
            sheet.setColumnWidth(3, (int) (12.0 * 256));
            sheet.setColumnWidth(4, (int) (12.0 * 256));

            Map<example.dsg_be.domain.teacher.domain.TeacherEntity, List<ApplyEntity>> groupedByTeacher = new LinkedHashMap<>();
            for (ApplyEntity apply : applyList) {
                groupedByTeacher.computeIfAbsent(apply.getTeacher(), k -> new ArrayList<>()).add(apply);
            }

            int rowNum = 4;
            for (Map.Entry<example.dsg_be.domain.teacher.domain.TeacherEntity, List<ApplyEntity>> entry : groupedByTeacher.entrySet()) {
                var teacher = entry.getKey();
                var applies = entry.getValue();
                int dinnerCount = 0;
                int dinnerSelfCount = 0;

                for (int i = 0; i < applies.size(); i++) {
                    ApplyEntity apply = applies.get(i);
                    Row dataRow = sheet.createRow(rowNum++);
                    dataRow.setHeightInPoints(17.4f);

                    ExcelUtil.createCellWithStyle(dataRow, 0, i == 0 ? teacher.getDepartment() : "", bodyStyle);
                    ExcelUtil.createCellWithStyle(dataRow, 1, i == 0 ? teacher.getName() : "", bodyStyle);
                    ExcelUtil.createCellWithStyle(dataRow, 2, apply.getCreatedAt().toLocalDate().toString(), bodyStyle);

                    if (apply.getMeal() == MealType.DINNER) {
                        ExcelUtil.createCellWithStyle(dataRow, 3, "O", bodyStyle);
                        ExcelUtil.createCellWithStyle(dataRow, 4, "", bodyStyle);
                        dinnerCount++;
                    } else { // DINNER_SELF
                        ExcelUtil.createCellWithStyle(dataRow, 3, "", bodyStyle);
                        ExcelUtil.createCellWithStyle(dataRow, 4, "O", bodyStyle);
                        dinnerSelfCount++;
                    }
                }

                Row subtotalRow = sheet.createRow(rowNum++);
                ExcelUtil.createCellWithStyle(subtotalRow, 0, "", bodyStyle);
                ExcelUtil.createCellWithStyle(subtotalRow, 1, "월계", bodyStyle);
                ExcelUtil.createCellWithStyle(subtotalRow, 2, "", bodyStyle);
                ExcelUtil.createCellWithStyle(subtotalRow, 3, String.valueOf(dinnerCount), bodyStyle);
                ExcelUtil.createCellWithStyle(subtotalRow, 4, String.valueOf(dinnerSelfCount), bodyStyle);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    // getMealLabel 사용되지 않아 삭제
}
