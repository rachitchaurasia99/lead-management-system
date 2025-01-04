package com.example.lead.management.system.models;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "calls")
public class Call {
    public enum Status{
        PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateTime;
    private Status status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @NonNull
    private String note;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public String getNote() {
        return note;
    }


    public void setContact(Contact contact) {
        this.contact = contact;
    }
    public Contact getContact() {
        return contact;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    public void setDateTime(Date date) {
        this.dateTime = Date.from(date.toInstant().atZone(ZoneId.of("UTC")).toInstant());
    }
    public Date getDateTime() {
        return dateTime;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() { return status; }

}
