package example.dsg_be.domain.apply.presentation;

import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.apply.presentation.dto.request.ApplyCreateRequest;
import example.dsg_be.domain.apply.presentation.dto.response.ApplyCreateResponse;
import example.dsg_be.domain.apply.presentation.dto.response.ApplyListResponse;
import example.dsg_be.domain.apply.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyCreateService applyCreateService;
    private final ApplyListReadService applyListReadService;
    private final ApplyDeleteService applyDeleteService;
    private final ApplyMonthlyExcelService applyMonthlyExcelService;
    private final ApplySummaryExcelService applySummaryExcelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplyCreateResponse create(@Valid @RequestBody ApplyCreateRequest request) {
        return applyCreateService.execute(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ApplyListResponse> getList(@RequestParam MealType meal) {
        return applyListReadService.execute(meal);
    }

    @DeleteMapping("/{apply-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("apply-id") Long applyId) {
        applyDeleteService.execute(applyId);
    }

    @GetMapping("/excel/monthly")
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

    @GetMapping("/excel/summary")
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
