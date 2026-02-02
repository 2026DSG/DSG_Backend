package example.dsg_be.domain.teacher.presentation;

import example.dsg_be.domain.teacher.presentation.dto.request.TeacherRequest;
import example.dsg_be.domain.teacher.presentation.dto.response.TeacherResponse;
import example.dsg_be.domain.teacher.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/admin/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherExcelUploadService teacherExcelUploadService;
    private final TeacherExcelUpdateService teacherExcelUpdateService;
    private final TeacherListReadService teacherListReadService;
    private final TeacherAddService teacherAddService;
    private final TeacherDeleteService teacherDeleteService;
    private final TeacherExcelDownloadService teacherExcelDownloadService;

    @PostMapping("/excel")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadExcel(@RequestParam("file") MultipartFile file) {
        teacherExcelUploadService.execute(file);
    }
    // TODO: .xlsx 형식이 아닌 파일 고려
    
    @PutMapping("/excel")
    @ResponseStatus(HttpStatus.OK)
    public void updateExcel(@RequestParam("file") MultipartFile file) {
        teacherExcelUpdateService.execute(file);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTeacher(@RequestBody TeacherRequest teacherRequest) {
        teacherAddService.execute(teacherRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TeacherResponse> getTeacherList() {
        return teacherListReadService.execute();
    }

    @DeleteMapping("/{teacher-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeacher(@PathVariable("teacher-id") Long id) {
        teacherDeleteService.execute(id);
    }

    @GetMapping("/excel")
    public ResponseEntity<Resource> downloadExcel() throws IOException { // TODO: Exception 대신 어노테이션 고려
        byte[] content = teacherExcelDownloadService.execute(); // 서비스에서 받아온 값을
        ByteArrayResource resource = new ByteArrayResource(content); // byte 배열로 body에 넣을 값을 변환 해준다.

        String fileName = URLEncoder.encode("교직원_목록.xlsx", StandardCharsets.UTF_8); // 파일명 설정, 인코딩 방법 설정

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) // Content-Type 설정
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"") // 해당 자원을 attachment : 다운 해야한다.
                .body(resource);
    }
}
