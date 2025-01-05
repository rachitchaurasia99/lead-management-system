package com.example.lead.management.system.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class Contact {
    public enum Enum {
        MANAGER, CHEF, OWNER
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    private String firstName;
    private String lastName;
    @Getter
    @Setter
    private String phone;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private Enum role;
    @Getter
    @Setter
    private String information;
    @Getter
    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;

    @OneToMany(mappedBy = "contact")
    private List<Call> calls;


    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Enum getRole() {
        return role;
    }

    public void setRole(Enum role) {
        this.role = role;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Lead getLead() {
        return lead;
    }
    public List<Call> getCalls() {
        return calls;
    }

    public void setCalls(List<Call> calls) { this.calls = calls; }
}
