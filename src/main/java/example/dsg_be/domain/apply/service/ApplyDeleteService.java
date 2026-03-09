package example.dsg_be.domain.apply.service;

import example.dsg_be.domain.apply.domain.ApplyEntity;
import example.dsg_be.domain.apply.exception.ApplyNotFoundException;
import example.dsg_be.domain.apply.repository.ApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyDeleteService {

    private final ApplyRepository applyRepository;

    @Transactional
    public void execute(Long applyId) {
        ApplyEntity applyEntity = applyRepository.findById(applyId)
                .orElseThrow(() -> ApplyNotFoundException.EXCEPTION);
        applyRepository.delete(applyEntity);
    }
}
