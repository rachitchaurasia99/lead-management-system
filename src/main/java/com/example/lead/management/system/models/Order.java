package com.example.lead.management.system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    public enum Status {
        PENDING,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED,
        COMPLETED;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date createdAt;
    private String description;
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private Status status;

    @PrePersist
    public void generateOrderNumber() {
        Date time = new Time(System.currentTimeMillis());
        this.orderNumber = "ORD" + time + "-" + UUID.randomUUID().toString().substring(0, 6);
        this.createdAt = time;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Lead getLead() { return lead; }
    public void setLead(Lead lead) { this.lead = lead; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
