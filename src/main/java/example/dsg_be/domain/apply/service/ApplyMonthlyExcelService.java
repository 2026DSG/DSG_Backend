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
import java.util.List;

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
            XSSFSheet sheet = workbook.createSheet("월별 신청 현황");

            CellStyle headerStyle = ExcelUtil.getHeaderStyle(workbook);
            CellStyle bodyStyle = ExcelUtil.getBodyStyle(workbook);

            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(17.4f);
            String[] headers = {"순번", "날짜", "급식 구분", "이름", "부서", "직위", "사유"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            sheet.setColumnWidth(0, (int) (8.3 * 256));
            sheet.setColumnWidth(1, (int) (15.0 * 256));
            sheet.setColumnWidth(2, (int) (14.0 * 256));
            sheet.setColumnWidth(3, (int) (13.4 * 256));
            sheet.setColumnWidth(4, (int) (13.8 * 256));
            sheet.setColumnWidth(5, (int) (9.7 * 256));
            sheet.setColumnWidth(6, (int) (30.0 * 256));

            int rowNum = 1;
            for (ApplyEntity apply : applyList) {
                Row dataRow = sheet.createRow(rowNum);
                dataRow.setHeightInPoints(17.4f);

                String date = apply.getCreatedAt().toLocalDate().toString();
                String mealLabel = getMealLabel(apply.getMeal());

                ExcelUtil.createCellWithStyle(dataRow, 0, String.valueOf(rowNum), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 1, date, bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 2, mealLabel, bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 3, apply.getTeacher().getName(), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 4, apply.getTeacher().getDepartment(), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 5, apply.getTeacher().getPosition(), bodyStyle);
                ExcelUtil.createCellWithStyle(dataRow, 6, apply.getReason(), bodyStyle);

                rowNum++;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private String getMealLabel(MealType mealType) {
        if (mealType == MealType.LUNCH) {
            return "중식";
        } else if (mealType == MealType.DINNER) {
            return "초과근무";
        } else {
            return "개인부담";
        }
    }
}
