package com.example.lead.management.system.repositories;

import com.example.lead.management.system.models.Contact;
import com.example.lead.management.system.models.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByLead(Lead lead);

    @Query("Select c from Contact c JOIN Lead l on c.lead.id = l.id WHERE l.user.id = :userId")
    List<Contact> findAllByUser(Long userId);
}
