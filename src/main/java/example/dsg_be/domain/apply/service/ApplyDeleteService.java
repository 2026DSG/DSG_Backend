package example.dsg_be.domain.apply.service;

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
    public void delete(Long applyId) {
        if (!applyRepository.existsById(applyId)) {
            throw new ApplyNotFoundException();
        }
        applyRepository.deleteById(applyId);
    }
}