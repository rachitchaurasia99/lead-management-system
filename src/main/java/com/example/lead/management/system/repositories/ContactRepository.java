package com.example.lead.management.system.repositories;

import com.example.lead.management.system.models.Contact;
import com.example.lead.management.system.models.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByLead(Lead lead);
}
