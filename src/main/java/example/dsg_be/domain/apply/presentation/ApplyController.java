package example.dsg_be.domain.apply.presentation;

import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.apply.presentation.dto.request.*;
import example.dsg_be.domain.apply.presentation.dto.response.*;
import example.dsg_be.domain.apply.service.ApplyCreateService;
import example.dsg_be.domain.apply.service.ApplyDeleteService;
import example.dsg_be.domain.apply.service.ApplyListReadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyCreateService applyCreateService;
    private final ApplyListReadService applyListReadService;
    private final ApplyDeleteService applyDeleteService;

    @PostMapping
    public ResponseEntity<ApplyCreateResponse> create(
            @RequestBody @Valid ApplyCreateRequest request) {
        ApplyCreateResponse response = applyCreateService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ApplyListResponse>> getList(
            @RequestParam MealType mealType) {
        List<ApplyListResponse> response = applyListReadService.getList(mealType);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{apply-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("apply-id") Long applyId) {
        applyDeleteService.delete(applyId);
        return ResponseEntity.noContent().build();
    }
}
