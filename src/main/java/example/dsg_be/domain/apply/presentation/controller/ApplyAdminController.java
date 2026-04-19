package example.dsg_be.domain.apply.presentation.controller;

import example.dsg_be.domain.apply.presentation.dto.response.ApplyListResponse;
import example.dsg_be.domain.apply.service.ApplyAdminReadService;
import example.dsg_be.domain.apply.service.ApplyMonthlyExcelService;
import example.dsg_be.domain.apply.service.ApplySummaryExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ApplyAdminController {
    private final ApplyMonthlyExcelService applyMonthlyExcelService;
    private final ApplySummaryExcelService applySummaryExcelService;
    private final ApplyAdminReadService applyAdminReadService;

    @GetMapping("/apply")
    public List<ApplyListResponse> getApplyListOnAdmin(@RequestParam LocalDate date) {
        return applyAdminReadService.execute(date);
    }

    @GetMapping("/apply/excel/monthly")
    public ResponseEntity<Resource> downloadMonthlyExcel(
            @RequestParam int year,
            @RequestParam int month) throws IOException {
        byte[] content = applyMonthlyExcelService.execute(year, month);
        ByteArrayResource resource = new ByteArrayResource(content);
        String fileName = URLEncoder.encode("월별_신청_현황.xlsx", StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/apply/excel/summary")
    public ResponseEntity<Resource> downloadSummaryExcel(
            @RequestParam int year,
            @RequestParam int month) throws IOException {
        byte[] content = applySummaryExcelService.execute(year, month);
        ByteArrayResource resource = new ByteArrayResource(content);
        String fileName = URLEncoder.encode("급식_신청_총괄표.xlsx", StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
