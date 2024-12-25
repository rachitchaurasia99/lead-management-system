package com.example.lead.management.system.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "calls")
public class Call {
    @Id
    private Long id;
    private @Getter @Setter Date date;
    @OneToOne
    @JoinColumn(name = "contact_id")
    private Restaurant contact;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
