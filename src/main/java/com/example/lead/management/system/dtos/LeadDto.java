package com.example.lead.management.system.dtos;

import com.example.lead.management.system.models.Call;
import com.example.lead.management.system.models.Contact;
import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.models.User;

import java.util.*;
import java.util.stream.Stream;

public class LeadDto {
    private Long id;
    private String name;
    private String address;
    private String description;
    private Lead.Status status;
    private User user;
    private Integer callFrequency;
    private List<Contact> contacts;

    public LeadDto(Long id, String name, String address, String description, Lead.Status status, User user, Integer callFrequency, List<Contact> contacts) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.status = status;
        this.user = user;
        this.callFrequency = callFrequency;
        this.contacts = contacts;
    }
    public LeadDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public LeadDto() {}

    public Set<Lead.Status> getAllowedStatuses() {
        return status != null ? status.getAllowedTransitions() : Collections.emptySet();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Lead.Status getCurrentStatus() {
        return status;
    }
    public void setCurrentStatus(Lead.Status status) { this.status = status; }

    public Lead.Status getStatus() { return status; }
    public void setStatus(Lead.Status status) { this.status = status; }
    public User getUser() { return  user; }
    public void setUser(User user) { this.user = user; }

    public Integer getCallFrequency() {
        return callFrequency;
    }
    public void setCallFrequency(Integer callFrequency) {
        this.callFrequency = callFrequency;
    }
    public Date lastCall() {
        return getCalls()
                .filter(call -> call.getStatus() == Call.Status.COMPLETED)
                .max(Comparator.comparing(Call::getDateTime))
                .map(Call::getDateTime)
                .orElse(null);
    }
    public Stream<Call> getCalls() {
        return contacts.stream().flatMap(contact -> contact.getCalls().stream());
    }
}
