package com.example.lead.management.system.dtos;

import com.example.lead.management.system.mapper.LeadMapper;
import com.example.lead.management.system.mapper.UserMapper;
import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.models.Order;
import com.example.lead.management.system.models.User;
import com.example.lead.management.system.services.LeadService;
import com.example.lead.management.system.services.UserService;
import jakarta.persistence.PrePersist;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public class OrderDto {

    private Long id;
    private Long leadId;
    private Long userId;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date createdAt;
    private Order.Status status;
    private LeadDto lead;
    private UserDto user;
    private String orderNumber;

    public OrderDto(Long id, Long leadId, Long userId, String description, Date createdAt, Order.Status status, String orderNumber) {
        this.id = id;
        this.leadId = leadId;
        this.userId = userId;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status;
        this.orderNumber = orderNumber;
    }

    public OrderDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLeadId() {
        return leadId;
    }

    public void setLeadId(Long leadId) {
        this.leadId = leadId;
    }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Order.Status getStatus() { return status; }
    public void setStatus(Order.Status status) { this.status = status; }
    public LeadDto getLeadDto(LeadService leadService) { return leadService.findById(leadId).get(); }
    public UserDto getUserDto(UserService userService) { return UserMapper.toDto(userService.findById(userId).get()); }
    public void setUser(UserDto user) { this.user = user; }
    public void setLead(LeadDto lead) { this.lead = lead; }
    public LeadDto getLead() { return lead; }
    public UserDto getUser() { return user; }
    public String getOrderedAt() { return createdAt.toString(); }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) {}
}
