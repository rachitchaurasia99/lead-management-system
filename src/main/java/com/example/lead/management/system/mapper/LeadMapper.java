package com.example.lead.management.system.mapper;

import com.example.lead.management.system.dtos.LeadDto;
import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.models.User;

public class LeadMapper {
    public static LeadDto toDTO(Lead lead) {
        return new LeadDto(
                lead.getId(),
                lead.getName(),
                lead.getAddress(),
                lead.getDescription(),
                lead.getStatus(),
                lead.getUser(),
                lead.getCallFrequency(),
                lead.getContacts()
        );
    }

    public static Lead toEntity(LeadDto leadDto) {
        Lead lead = new Lead();
        lead.setId(leadDto.getId());
        lead.setName(leadDto.getName());
        lead.setAddress(leadDto.getAddress());
        lead.setDescription(leadDto.getDescription());
        lead.setCurrentStatus(leadDto.getCurrentStatus());
        lead.setUser(leadDto.getUser());
        lead.setCallFrequency(leadDto.getCallFrequency());

        return lead;
    }
}
