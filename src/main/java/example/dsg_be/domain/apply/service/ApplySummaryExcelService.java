package example.dsg_be.domain.apply.service;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.apply.exception.ApplyNotFoundException;
import example.dsg_be.domain.apply.repository.ApplyRepository;
import example.dsg_be.domain.teacher.domain.TeacherEntity;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplySummaryExcelService {

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

        Map<TeacherEntity, long[]> summaryMap = new LinkedHashMap<>();
        for (ApplyEntity apply : applyList) {
            TeacherEntity teacher = apply.getTeacher();
            summaryMap.putIfAbsent(teacher, new long[]{0L, 0L});
            long[] counts = summaryMap.get(teacher);

            if (apply.getMeal() == MealType.DINNER) {
                counts[0]++;
            } else { // DINNER_SELF
                counts[1]++;
            }
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("총괄표");

            CellStyle headerStyle = ExcelUtil.getHeaderStyle(workbook);
            CellStyle bodyStyle = ExcelUtil.getBodyStyle(workbook);

            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("총괄표");

            Row dateRow = sheet.createRow(1);
            dateRow.createCell(0).setCellValue(year + "-" + month + "월");

            Row headerRow = sheet.createRow(2);
            headerRow.setHeightInPoints(17.4f);
            String[] headers = {"부서", "이름", "초과근무", "개인부담"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            sheet.setColumnWidth(0, (int) (13.8 * 256));
            sheet.setColumnWidth(1, (int) (13.4 * 256));
            sheet.setColumnWidth(2, (int) (12.0 * 256));
            sheet.setColumnWidth(3, (int) (12.0 * 256));

            int rowNum = 3;
            long grandTotalDinner = 0L;
            long grandTotalSelf = 0L;

            for (Map.Entry<TeacherEntity, long[]> entry : summaryMap.entrySet()) {
                TeacherEntity teacher = entry.getKey();
                long dinnerCount = entry.getValue()[0];
                long selfCount = entry.getValue()[1];

                grandTotalDinner += dinnerCount;
                grandTotalSelf += selfCount;

                Row dataRow = sheet.createRow(rowNum++);
                dataRow.setHeightInPoints(17.4f);

                ExcelUtil.createCellWithStyle(dataRow, 0, teacher.getDepartment(), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 1, teacher.getName(), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 2, String.valueOf(dinnerCount), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 3, String.valueOf(selfCount), bodyStyle);
            }

            Row totalRow = sheet.createRow(rowNum);
            totalRow.setHeightInPoints(17.4f);
            ExcelUtil.createCellWithStyle(totalRow, 0, "", bodyStyle);
            ExcelUtil.createCellWithStyle(totalRow, 1, "총계", bodyStyle);
            ExcelUtil.createCellWithStyle(totalRow, 2, String.valueOf(grandTotalDinner), bodyStyle);
            ExcelUtil.createCellWithStyle(totalRow, 3, String.valueOf(grandTotalSelf), bodyStyle);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
