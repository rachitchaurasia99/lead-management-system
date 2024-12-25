package com.example.lead.management.system.services;

import com.example.lead.management.system.models.Contact;
import com.example.lead.management.system.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Optional<Contact> findById(Long id) {
        return contactRepository.findById(id);
    }

    public void save(Contact contact) {
        contactRepository.save(contact);
    }
}
