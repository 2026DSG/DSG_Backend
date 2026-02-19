package example.dsg_be.global.util;

import example.dsg_be.domain.teacher.exception.NameCellIsEmptyException;
import example.dsg_be.domain.teacher.exception.NotExcelFileException;
import example.dsg_be.domain.teacher.presentation.dto.ExcelReadData;
import example.dsg_be.global.error.excpetion.BusinessException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ExcelDataUtil {
    public static List<ExcelReadData> readExcel (MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".xlsx")) {
            throw NotExcelFileException.Exception;
        }

        try(XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            List<ExcelReadData> dataList = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
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

                dataList.add(new ExcelReadData(name, department, position));
            }
            return dataList;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw NotExcelFileException.Exception;
        }
    }
}
