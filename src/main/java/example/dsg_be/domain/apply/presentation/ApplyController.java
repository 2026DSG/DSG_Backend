package example.dsg_be.domain.apply.presentation;

import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.apply.presentation.dto.request.ApplyCreateRequest;
import example.dsg_be.domain.apply.presentation.dto.response.ApplyCreateResponse;
import example.dsg_be.domain.apply.presentation.dto.response.ApplyListResponse;
import example.dsg_be.domain.apply.service.ApplyCreateService;
import example.dsg_be.domain.apply.service.ApplyDeleteService;
import example.dsg_be.domain.apply.service.ApplyListReadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
}
