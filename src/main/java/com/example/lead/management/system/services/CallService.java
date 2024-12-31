package com.example.lead.management.system.services;

import com.example.lead.management.system.models.Call;
import com.example.lead.management.system.repositories.CallRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CallService {
    private final CallRepository callRepository;
    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }
    @Transactional
    public Call save(Call call) {
        return callRepository.save(call);
    }
    public Optional<Call> findById(Long id) {
        return callRepository.findById(id);
    }
    public Page<Call> findAll(PageRequest pageRequest) {
        return callRepository.findAll(pageRequest);
    }
    public Page<Call> findAllByTime(PageRequest pageRequest) {
        PageRequest sortedPageRequest = PageRequest.of(
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return callRepository.findAll(sortedPageRequest);
    }
    public void deleteById(Long id) {
        callRepository.deleteById(id);
    }
}
