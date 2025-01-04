package com.example.lead.management.system.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Stream;

import static com.example.lead.management.system.models.Call.Status.COMPLETED;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leads")
public class Lead {

    public enum Status {
        NEW {
            @Override
            public Set<Status> getAllowedTransitions() {
                return Set.of(CONTACTED);
            }
        },
        CONTACTED {
            @Override
            public Set<Status> getAllowedTransitions() {
                return Set.of(QUALIFIED, LOST);
            }
        },
        QUALIFIED {
            @Override
            public Set<Status> getAllowedTransitions() {
                return Set.of(IN_PROGRESS, LOST);
            }
        },
        IN_PROGRESS {
            @Override
            public Set<Status> getAllowedTransitions() {
                return Set.of(CONVERTED, LOST);
            }
        },
        CONVERTED {
            @Override
            public Set<Status> getAllowedTransitions() {
                return Collections.emptySet();
            }
        },
        LOST {
            @Override
            public Set<Status> getAllowedTransitions() {
                return Collections.emptySet();
            }
        },
        DORMANT {
            @Override
            public Set<Status> getAllowedTransitions() {
                return Set.of(CONTACTED);
            }
        };

        public abstract Set<Status> getAllowedTransitions();

        public boolean canTransitionTo(Status nextStatus) {
            return getAllowedTransitions().contains(nextStatus);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    private String description;

    @Min(value = 1, message = "Call Frequency must be at least 1")
    private Integer callFrequency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "lead")
    private List<Contact> contacts;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @Transient
    private List<String> errors;

    @OneToMany(mappedBy = "lead")
    private List<Order> orders;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.status = currentStatus;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }

    public Date lastCall() {
        return getCalls()
                .filter(call -> call.getStatus() == Call.Status.COMPLETED)
                .max(Comparator.comparing(Call::getDateTime))
                .map(Call::getDateTime)
                .orElse(null);
    }

    public String getDescription() { return description; }
    public Integer getCallFrequency() { return callFrequency; }
    public User getUser() { return user; }


    public List<Contact> getContacts() {
        return contacts;
    }

    public Status getStatus() { return status; }

    public Stream<Call> getCalls() {
        return contacts.stream().flatMap(contact -> contact.getCalls().stream());
    }


    public void setCallFrequency(Integer callFrequency) {
        this.callFrequency = callFrequency;
    }

    public void setUser(User user) { this.user = user; }

    public void setContacts(List<Contact> contacts) { this.contacts = contacts; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public void setDescription(String description) { this.description = description; }


    public void setStatus(Status status) { this.status = status; }

    public Set<Status> getAllowedStatuses() {
        return status != null ? status.getAllowedTransitions() : Collections.emptySet();
    }

    public Status getCurrentStatus() {
        return this.status;
    }

    public void transitionStatus(Status nextStatus) {
        if (this.status == null) {
            this.status = nextStatus;
            return;
        }

        if (!this.status.canTransitionTo(nextStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot transition from %s to %s", this.status, nextStatus)
            );
        }
        this.status = nextStatus;
    }
}
