package com.example.lead.management.system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    public enum Role{
        ADMIN, USER
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(nullable=false, unique=true)
    private String email;
    private Date dob;
    private Date joinDate;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    @Column(nullable=false)
    private String password;
    private String phone;
    private String address;
    private Role role;
    private String timeZone;
    @Transient
    private String matchingPassword;

    @OneToMany(mappedBy = "user")
    private List<Lead> leads;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
    public Role getRole() {
        return role;
    }
    public String getMatchingPassword() { return matchingPassword; }
    public void setMatchingPassword(String matchingPassword) { this.matchingPassword = matchingPassword; }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getLastName() {
        return lastName;
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoinDate(){
        return joinDate;
    }

    public String getTimeZone() { return timeZone; }
    public void setTimeZone(String timeZone) { this.timeZone = timeZone; }

    public String getFullName() { return firstName + " " + lastName; }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public void setRole(String role) { this.role = Role.valueOf(role); }

    public List<Role> getRoles(){ return List.of(role); }
    public void setJoinDate(Date joinDate){ this.joinDate = joinDate; }

    public Long getSuccessfulConversions(){
        return leads.stream().filter(lead -> lead.getStatus() == Lead.Status.CONVERTED).count();
    }


    public List<Lead> getLeads() {
        return leads;
    }
}
