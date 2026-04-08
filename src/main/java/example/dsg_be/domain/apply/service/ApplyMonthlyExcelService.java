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
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplyMonthlyExcelService {

    private static final long MEAL_PRICE = 5_500L;

    private final ApplyRepository applyRepository;

    @Transactional(readOnly = true)
    public byte[] execute(int year, int month) throws IOException {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

        List<ApplyEntity> applyList = applyRepository
                .findAllByCreatedAtBetweenOrderByCreatedAtAsc(start, end);

        if (applyList.isEmpty()) {
            throw ApplyNotFoundException.EXCEPTION;
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("월별 개인별 목록");

            CellStyle headerStyle = ExcelUtil.getHeaderStyle(workbook);
            CellStyle bodyStyle = ExcelUtil.getBodyStyle(workbook);

            DataFormat dataFormat = workbook.createDataFormat();

            CellStyle numberStyle = ExcelUtil.getBodyStyle(workbook);
            numberStyle.setDataFormat(dataFormat.getFormat("#,##0"));

            CellStyle blankZeroNumberStyle = ExcelUtil.getBodyStyle(workbook);
            blankZeroNumberStyle.setDataFormat(dataFormat.getFormat("#,##0;-#,##0;;@"));

            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("월별 개인별 목록");

            Row dateRow = sheet.createRow(2);
            dateRow.createCell(0).setCellValue(year + "-" + month + "월");

            Row headerRow = sheet.createRow(3);
            headerRow.setHeightInPoints(17.4f);
            String[] headers = {"부서", "직위", "이름", "일자", "초과근무", "개인부담", "금액"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            sheet.setColumnWidth(0, (int) (13.8 * 256));
            sheet.setColumnWidth(1, (int) (9.7 * 256));
            sheet.setColumnWidth(2, (int) (13.4 * 256));
            sheet.setColumnWidth(3, (int) (15.0 * 256));
            sheet.setColumnWidth(4, (int) (12.0 * 256));
            sheet.setColumnWidth(5, (int) (12.0 * 256));
            sheet.setColumnWidth(6, (int) (12.0 * 256));

            Map<TeacherEntity, List<ApplyEntity>> groupedByTeacher = new LinkedHashMap<>();
            for (ApplyEntity apply : applyList) {
                groupedByTeacher
                        .computeIfAbsent(apply.getTeacher(), k -> new ArrayList<>())
                        .add(apply);
            }

            int rowNum = 4;
            for (Map.Entry<TeacherEntity, List<ApplyEntity>> entry : groupedByTeacher.entrySet()) {
                TeacherEntity teacher = entry.getKey();
                List<ApplyEntity> applies = entry.getValue();

                int dinnerCount = 0;
                int selfCount = 0;
                long totalAmount = 0L;

                for (int i = 0; i < applies.size(); i++) {
                    ApplyEntity apply = applies.get(i);
                    Row dataRow = sheet.createRow(rowNum++);
                    dataRow.setHeightInPoints(17.4f);

                    boolean isSelf = (apply.getMeal() == MealType.LUNCH_SELF
                            || apply.getMeal() == MealType.DINNER_SELF);
                    long rowAmount = isSelf ? MEAL_PRICE : 0L;

                    if (isSelf) {
                        selfCount++;
                        totalAmount += MEAL_PRICE;
                    } else {
                        dinnerCount++;
                    }

                    ExcelUtil.createCellWithStyle(dataRow, 0, i == 0 ? teacher.getDepartment() : "", bodyStyle);
                    ExcelUtil.createCellWithStyle(dataRow, 1, i == 0 ? teacher.getPosition() : "", bodyStyle);
                    ExcelUtil.createCellWithStyle(dataRow, 2, i == 0 ? teacher.getName() : "", bodyStyle);
                    ExcelUtil.createCellWithStyle(dataRow, 3, apply.getCreatedAt().toLocalDate().toString(), bodyStyle);

                    if (isSelf) {
                        ExcelUtil.createCellWithStyle(dataRow, 4, "", bodyStyle);
                        ExcelUtil.createCellWithStyle(dataRow, 5, "O", bodyStyle);
                    } else {
                        ExcelUtil.createCellWithStyle(dataRow, 4, "O", bodyStyle);
                        ExcelUtil.createCellWithStyle(dataRow, 5, "", bodyStyle);
                    }

                    Cell amountCell = dataRow.createCell(6);
                    amountCell.setCellValue(rowAmount);
                    amountCell.setCellStyle(rowAmount > 0 ? numberStyle : blankZeroNumberStyle);
                }

                Row subtotalRow = sheet.createRow(rowNum++);
                ExcelUtil.createCellWithStyle(subtotalRow, 0, "", bodyStyle);
                ExcelUtil.createCellWithStyle(subtotalRow, 1, "", bodyStyle);
                ExcelUtil.createCellWithStyle(subtotalRow, 2, "월계", bodyStyle);
                ExcelUtil.createCellWithStyle(subtotalRow, 3, "", bodyStyle);
                ExcelUtil.createCellWithStyle(subtotalRow, 4, String.valueOf(dinnerCount), bodyStyle);
                ExcelUtil.createCellWithStyle(subtotalRow, 5, String.valueOf(selfCount), bodyStyle);

                Cell subtotalAmountCell = subtotalRow.createCell(6);
                subtotalAmountCell.setCellValue(totalAmount);
                subtotalAmountCell.setCellStyle(numberStyle);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
