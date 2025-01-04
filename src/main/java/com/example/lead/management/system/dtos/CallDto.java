package com.example.lead.management.system.dtos;

import com.example.lead.management.system.models.Call;
import com.example.lead.management.system.models.Contact;
import com.example.lead.management.system.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallDto {
    private Long id;
    private Long userId;
    private Long contactId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateTime;
    private Call.Status status;
    private String note;
    private Contact contact;
    private User user;

    public CallDto(Long id, Long userId, Long contactId, Date dateTime, Call.Status status, String note, Contact contact, User user) {
        this.id = id;
        this.userId = userId;
        this.contactId = contactId;
        this.dateTime = dateTime;
        this.status = status;
        this.note = note;
        this.contact = contact;
        this.user = user;
    }

    public CallDto() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getContactId() { return contactId; }
    public void setContactId(Long contactId) { this.contactId = contactId; }
    public Date getDateTime() { return dateTime; }
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }
    public Call.Status getStatus() { return status; }
    public void setStatus(Call.Status status) { this.status = status; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Contact getContact() { return contact; }
    public void setContact(Contact contact) { this.contact = contact; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getConvertToUserTimeZone(String userTimezone) {
        ZonedDateTime utcDateTime = dateTime.toInstant().atZone(ZoneId.of("UTC"));
        return utcDateTime.withZoneSameInstant(ZoneId.of(userTimezone)).toString();
    }
}
