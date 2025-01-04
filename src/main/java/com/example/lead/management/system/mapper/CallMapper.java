package com.example.lead.management.system.mapper;

import com.example.lead.management.system.dtos.CallDto;
import com.example.lead.management.system.models.Call;
import com.example.lead.management.system.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CallMapper {
    private final UserRepository userRepository;

    public CallMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CallDto toDto(Call call) {
        return new CallDto(
                call.getId(),
                call.getUser() != null ? call.getUser().getId() : null,
                call.getContact() != null ? call.getContact().getId() : null,
                call.getDateTime(),
                call.getStatus() != null ? call.getStatus() : null,
                call.getNote(),
                call.getContact(),
                call.getUser()
        );
    }

    public Call toEntity(CallDto callDto) {
        Call call = new Call();
        call.setId(callDto.getId());
        call.setDateTime(callDto.getDateTime());
        call.setNote(callDto.getNote());
        call.setStatus(callDto.getStatus());

        userRepository.findById(callDto.getUserId()).ifPresent(call::setUser);
        return call;
    }
}
