package com.example.lead.management.system.services;

import com.example.lead.management.system.dtos.CallDto;
import com.example.lead.management.system.mapper.CallMapper;
import com.example.lead.management.system.models.Call;
import com.example.lead.management.system.repositories.CallRepository;
import com.example.lead.management.system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CallService {

    private final CallRepository callRepository;
    private final CallMapper callMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    public CallService(CallRepository callRepository, CallMapper callMapper, UserRepository userRepository, UserService userService) {
        this.callRepository = callRepository;
        this.callMapper = callMapper;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public void save(CallDto callDto) {
        Call call = callMapper.toEntity(callDto);
        callRepository.save(call);
    }

    public Optional<Call> findById(Long id) {
        return callRepository.findById(id);
    }

    public Page<CallDto> findAllByTime(Long leadId, Long userId, PageRequest pageRequest) {
        PageRequest sortedPageRequest = pageRequest.withSort(Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Call> calls = Optional.ofNullable(userId)
                .flatMap(userRepository::findById)
                .map(user -> callRepository.findAllByUser(user, sortedPageRequest))
                .orElseGet(() -> leadId != null
                        ? callRepository.findAllByLead(leadId, sortedPageRequest)
                        : callRepository.findAll(sortedPageRequest));

        List<CallDto> callDtos = calls.stream()
                .map(callMapper::toDto)
                .filter(callDto -> userService.currentUser().get().isAdmin() ||
                        userService.currentUser().get().getId().equals(callDto.getUserId()))
                .collect(Collectors.toList());

        return new PageImpl<>(callDtos, pageRequest, calls.getTotalElements());
    }


    @Transactional
    public void deleteById(Long id) {
        callRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, CallDto callDto) {
        Call existingCall = callRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Call not found"));

        updateCallFields(existingCall, callDto);
        callRepository.save(existingCall);
    }

    private void updateCallFields(Call existingCall, CallDto callDto) {
        existingCall.setDateTime(callDto.getDateTime());
        existingCall.setContact(callDto.getContact());
        existingCall.setStatus(callDto.getStatus());
        existingCall.setNote(callDto.getNote());
    }
}
