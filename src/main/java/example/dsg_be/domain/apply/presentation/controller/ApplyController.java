package example.dsg_be.domain.apply.presentation.controller;

import example.dsg_be.domain.apply.domain.MealType;
import example.dsg_be.domain.apply.presentation.dto.request.ApplyCreateRequest;
import example.dsg_be.domain.apply.presentation.dto.response.ApplyListResponse;
import example.dsg_be.domain.apply.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyCreateService applyCreateService;
    private final ApplyListReadService applyListReadService;
    private final ApplyDeleteService applyDeleteService;

    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody ApplyCreateRequest request) {
        applyCreateService.execute(request);
    }

    @GetMapping("/apply")
    @ResponseStatus(HttpStatus.OK)
    public List<ApplyListResponse> getList(@RequestParam MealType meal) {
        return applyListReadService.execute(meal);
    }

    @DeleteMapping("/apply/{apply-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("apply-id") Long applyId) {
        applyDeleteService.execute(applyId);
    }
}
