package com.example.lead.management.system.services;

import com.example.lead.management.system.dtos.LeadDto;
import com.example.lead.management.system.mapper.LeadMapper;
import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.models.User;
import com.example.lead.management.system.repositories.LeadRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final UserService userService;
    private final Validator validator;

    public LeadService(LeadRepository leadRepository, UserService userService) {
        this.leadRepository = leadRepository;
        this.userService = userService;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }
    
    public Page<LeadDto> findAll(PageRequest pageRequest) {
        return leadRepository.findAll(pageRequest).map(LeadMapper::toDTO);
    }
    
    public List<LeadDto> findAll() {
        return leadRepository.findAll().stream().map(LeadMapper::toDTO).collect(Collectors.toList());
    }
    
    public Optional<LeadDto> findById(Long id) {
        return leadRepository.findById(id).map(LeadMapper::toDTO);
    }
    
    public void save(Lead lead) {
        Optional<User> user = userService.findById(lead.getUser() != null ? lead.getUser().getId() : null);
        user.ifPresent(lead::setUser);
        leadRepository.save(lead);
    }

    public void update(Long id, Lead lead) {
        Optional<Lead> optionalLead = leadRepository.findById(id);
        if (optionalLead.isPresent()) {
            Set<ConstraintViolation<Lead>> violations = validator.validate(lead);
            if (!violations.isEmpty()) {
                List<String> errorMessages = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
                lead.setErrors(errorMessages);
                return;
            }

            Lead existingLead = optionalLead.get();

            existingLead.setName(lead.getName());
            existingLead.setAddress(lead.getAddress());
            existingLead.setDescription(lead.getDescription());
            existingLead.setStatus(lead.getStatus());
            existingLead.setCallFrequency(lead.getCallFrequency());

            if (lead.getUser() != null && lead.getUser().getId() != null) {
                Optional<User> user = userService.findById(lead.getUser().getId());
                user.ifPresent(existingLead::setUser);
            }
            leadRepository.save(existingLead);
        } else {
            throw new IllegalArgumentException("Lead with ID " + id + " not found.");
        }
    }
    
    public void deleteById(Long id) {
        leadRepository.deleteById(id);
    }
    
    public Page<LeadDto> findByName(String query, PageRequest pageRequest) {
        return leadRepository.findAllByName(query, pageRequest).map(LeadMapper::toDTO);
    }
    
    public Page<LeadDto> findByUserAndName(User user, String query, PageRequest pageRequest) {
        return leadRepository.findByUserAndName(user, query, pageRequest).map(LeadMapper::toDTO);
    }
    
    public Page<LeadDto> findAllByUser(User user, PageRequest pageRequest) {
        return leadRepository.findAllByUser(user, pageRequest).map(LeadMapper::toDTO);
    }

    public List<LeadDto> findAllByUser(User user) {
        return leadRepository.findAllByUser(user).stream().map(LeadMapper::toDTO).collect(Collectors.toList());
    }

    public List<Lead> findAllByIds(List<Long> leadIds) {
        return leadRepository.findAllByIds(leadIds);
    }

    public Optional<Lead> findByIdAndUser(Long id, User user) {
        return leadRepository.findByIdAndUser(id, user);
    }
}
