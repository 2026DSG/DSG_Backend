package example.dsg_be.domain.apply.service;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.exception.AlreadyAppliedException;
import example.dsg_be.domain.apply.presentation.dto.request.ApplyCreateRequest;
import example.dsg_be.domain.apply.presentation.dto.response.ApplyCreateResponse;
import example.dsg_be.domain.apply.repository.ApplyRepository;
import example.dsg_be.domain.teacher.domain.TeacherEntity;
import example.dsg_be.domain.teacher.exception.TeacherNotFoundException;
import example.dsg_be.domain.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyCreateService {

    private final ApplyRepository applyRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public ApplyCreateResponse execute(ApplyCreateRequest request) {
        TeacherEntity teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> TeacherNotFoundException.EXCEPTION);

        // 동일 날짜, 동일 교직원이면 중복 신청 불가
        if (applyRepository.existsByTeacherAndApplyDate(teacher, request.getApplyDate())) {
            throw AlreadyAppliedException.EXCEPTION;
        }

        ApplyEntity applyEntity = ApplyEntity.builder()
                .teacher(teacher)
                .meal(request.getMeal())
                .reason(request.getReason())
                .applyDate(request.getApplyDate())
                .build();

        ApplyEntity saved = applyRepository.save(applyEntity);
        return new ApplyCreateResponse(saved);
    }
}
