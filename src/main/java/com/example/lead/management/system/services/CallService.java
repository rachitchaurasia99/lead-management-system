package com.example.lead.management.system.services;

import com.example.lead.management.system.models.Call;
import com.example.lead.management.system.repository.CallRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CallService {
    private CallRepository callRepository;
    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }
    public Call save(Call call) {
        return callRepository.save(call);
    }
    public Optional<Call> findById(Long id) {
        return callRepository.findById(id);
    }
    public List<Call> findAll() {
        return callRepository.findAll();
    }
    public void deleteById(Long id) {
        callRepository.deleteById(id);
    }
}
