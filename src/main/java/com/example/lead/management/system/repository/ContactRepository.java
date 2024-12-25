package com.example.lead.management.system.repository;

import com.example.lead.management.system.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
