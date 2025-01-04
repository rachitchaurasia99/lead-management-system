package com.example.lead.management.system.dtos;

import com.example.lead.management.system.models.Lead;

import java.util.Date;
import java.util.List;

public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String timeZone;
    private String role;
    private Date joinDate;
    private List<Lead> leads;
    private String password;
    private String matchingPassword;

    public UserDto(Long id, String firstName, String lastName, String email, String phone, String timeZone, String role, Date joinDate, List<Lead> leads, String password, String matchingPassword) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.timeZone = timeZone;
        this.role = role;
        this.joinDate = joinDate;
        this.leads = leads;
        this.password = password;
        this.matchingPassword = matchingPassword;
    }

    public UserDto() {
    }

    public Date getJoinDate() {
        return joinDate;
    }
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Lead> getLeads() { return leads; }
    public void setLeads(List<Lead> leads) { this.leads = leads; }

    public Long getSuccessfulConversions(){
        return leads.stream().filter(lead -> lead.getStatus() == Lead.Status.CONVERTED).count();
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) { this.password = password; }
    public String getMatchingPassword() { return matchingPassword; }
    public void setMatchingPassword(String matchingPassword) { this.matchingPassword = matchingPassword; }
    public String fullName() { return firstName + " " + lastName; }
}
