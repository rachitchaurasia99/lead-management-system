package com.example.lead.management.system.services;

import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.repositories.LeadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LeadService {
    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public Page<Lead> findAll(PageRequest pageRequest) {
        return leadRepository.findAll(pageRequest);
    }

    public Optional<Lead> findById(Long id) {
        return leadRepository.findById(id);
    }

    public void save(Lead lead) {
        leadRepository.save(lead);
    }

    public void deleteById(Long id) {
        leadRepository.deleteById(id);
    }

    public void update(Lead lead) {
        leadRepository.save(lead);
    }

    public Page<Lead> findByNameContaining(String query, PageRequest pageRequest) {
        return leadRepository.findAllByNameContaining(query, pageRequest);
    }
}
