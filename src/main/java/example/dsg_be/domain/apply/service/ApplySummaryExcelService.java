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

    private static final long MEAL_PRICE = 5_500L;

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

        // 횟수 집계
        Map<TeacherEntity, long[]> summaryMap = new LinkedHashMap<>();
        for (ApplyEntity apply : applyList) {
            TeacherEntity teacher = apply.getTeacher();
            summaryMap.putIfAbsent(teacher, new long[]{0L, 0L, 0L});
            long[] counts = summaryMap.get(teacher);

            if (apply.getMeal() == MealType.LUNCH) {
                counts[0]++;
            } else if (apply.getMeal() == MealType.DINNER) {
                counts[1]++;
            } else {
                counts[2]++; // DINNER_SELF
            }
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("급식 신청 총괄표");

            CellStyle headerStyle = ExcelUtil.getHeaderStyle(workbook);
            CellStyle bodyStyle = ExcelUtil.getBodyStyle(workbook);

            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(17.4f);
            String[] headers = {
                    "순번", "부서", "직위", "이름",
                    "중식 횟수", "석식 횟수", "석식(개인) 횟수",
                    "총 횟수", "결제 예정 금액"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            sheet.setColumnWidth(0, (int) (8.3 * 256));
            sheet.setColumnWidth(1, (int) (13.8 * 256));
            sheet.setColumnWidth(2, (int) (9.7 * 256));
            sheet.setColumnWidth(3, (int) (13.4 * 256));
            sheet.setColumnWidth(4, (int) (10.0 * 256));
            sheet.setColumnWidth(5, (int) (10.0 * 256));
            sheet.setColumnWidth(6, (int) (14.0 * 256));
            sheet.setColumnWidth(7, (int) (10.0 * 256));
            sheet.setColumnWidth(8, (int) (16.0 * 256));

            int rowNum = 1;
            long grandTotalCount = 0L;
            long grandTotalAmount = 0L;

            for (Map.Entry<TeacherEntity, long[]> entry : summaryMap.entrySet()) {
                TeacherEntity teacher = entry.getKey();
                long lunchCount = entry.getValue()[0];
                long dinnerCount = entry.getValue()[1];
                long dinnerSelfCount = entry.getValue()[2];
                long total = lunchCount + dinnerCount + dinnerSelfCount;
                long amount = dinnerSelfCount * MEAL_PRICE; // DINNER_SELF만 과금

                grandTotalCount += total;
                grandTotalAmount += amount;

                Row dataRow = sheet.createRow(rowNum);
                dataRow.setHeightInPoints(17.4f);

                ExcelUtil.createCellWithStyle(dataRow, 0, String.valueOf(rowNum), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 1, teacher.getDepartment(), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 2, teacher.getPosition(), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 3, teacher.getName(), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 4, String.valueOf(lunchCount), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 5, String.valueOf(dinnerCount), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 6, String.valueOf(dinnerSelfCount), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 7, String.valueOf(total), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 8, amount + "원", bodyStyle);

                rowNum++;
            }

            // 하단 합계 행
            Row totalRow = sheet.createRow(rowNum);
            totalRow.setHeightInPoints(17.4f);

            ExcelUtil.createCellWithStyle(totalRow, 0, "", headerStyle);
            ExcelUtil.createCellWithStyle(totalRow, 1, "", headerStyle);
            ExcelUtil.createCellWithStyle(totalRow, 2, "", headerStyle);
            ExcelUtil.createCellWithStyle(totalRow, 3, "합계", headerStyle);
            ExcelUtil.createCellWithStyle(totalRow, 4, "", headerStyle);
            ExcelUtil.createCellWithStyle(totalRow, 5, "", headerStyle);
            ExcelUtil.createCellWithStyle(totalRow, 6, "", headerStyle);
            ExcelUtil.createCellWithStyle(totalRow, 7, String.valueOf(grandTotalCount), headerStyle);
            ExcelUtil.createCellWithStyle(totalRow, 8, grandTotalAmount + "원", headerStyle);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
