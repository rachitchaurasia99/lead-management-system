package com.example.lead.management.system.controllers;

import com.example.lead.management.system.mapper.LeadMapper;
import com.example.lead.management.system.models.Contact;
import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.services.ContactService;
import com.example.lead.management.system.services.LeadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ContactControllerTest {

    @InjectMocks
    private ContactController contactController;

    @Mock
    private ContactService contactService;

    @Mock
    private LeadService leadService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    private Lead lead;
    private Contact contact;

    @BeforeEach
    public void setUp() {
        lead = new Lead();
        lead.setId(1L);
        contact = new Contact();
        contact.setId(1L);
        contact.setLead(lead);
    }


    @Test
    public void testCreateContact_LeadNotFound() {
        when(leadService.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> contactController.create(1L, model));
        assertEquals("Lead not found", exception.getMessage());
    }

    @Test
    public void testSaveContact_LeadExists() {
        when(leadService.findById(1L)).thenReturn(Optional.of(LeadMapper.toDTO(lead)));

        String viewName = contactController.save(1L, contact, model);

        verify(contactService).save(contact);
        assertEquals("redirect:/contacts", viewName);
    }

    @Test
    public void testSaveContact_LeadNotFound() {
        when(leadService.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> contactController.save(1L, contact, model));
        assertEquals("Lead not found", exception.getMessage());
    }


    @Test
    public void testEditContact_LeadExists_ContactNotFound() {
        when(leadService.findById(1L)).thenReturn(Optional.of(LeadMapper.toDTO(lead)));
        when(contactService.findById(1L)).thenReturn(Optional.empty());

        String viewName = contactController.edit(1L, 1L, model, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Contact not found.");
        assertEquals("redirect:/contacts", viewName);
    }

    @Test
    public void testDeleteContact_LeadExists_ContactExists() {
        when(leadService.findById(1L)).thenReturn(Optional.of(LeadMapper.toDTO(lead)));
        when(contactService.findById(1L)).thenReturn(Optional.of(contact));

        String viewName = contactController.delete(1L, 1L, redirectAttributes, model);

        verify(contactService).deleteById(contact.getId());
        assertEquals("contacts", viewName);
    }

    @Test
    public void testDeleteContact_LeadExists_ContactNotFound() {
        when(leadService.findById(1L)).thenReturn(Optional.of(LeadMapper.toDTO(lead)));
        when(contactService.findById(1L)).thenReturn(Optional.empty());

        String viewName = contactController.delete(1L, 1L, redirectAttributes, model);

        verify(redirectAttributes).addFlashAttribute("error", "Contact not found.");
        assertEquals("contacts", viewName);
    }

    @Test
    public void testDeleteContact_LeadNotFound() {
        when(leadService.findById(1L)).thenReturn(Optional.empty());

        String viewName = contactController.delete(1L, 1L, redirectAttributes, model);

        verify(redirectAttributes).addFlashAttribute("error", "Lead not found.");
        assertEquals("contacts", viewName);
    }

    @Test
    public void testUpdateContact_LeadExists_ContactExists() {
        Contact updatedContact = new Contact();
        updatedContact.setFirstName("Updated");
        updatedContact.setLastName("Contact");

        when(leadService.findById(1L)).thenReturn(Optional.of(LeadMapper.toDTO(lead)));
        when(contactService.findById(1L)).thenReturn(Optional.of(contact));

        String viewName = contactController.update(1L, 1L, updatedContact, redirectAttributes);

        verify(contactService).save(contact);
        verify(redirectAttributes).addFlashAttribute("success", "Contact Updated.");
        assertEquals("contact-form", viewName);
    }

    @Test
    public void testUpdateContact_LeadExists_ContactNotFound() {
        Contact updatedContact = new Contact();
        updatedContact.setFirstName("Updated");
        updatedContact.setLastName("Contact");

        when(leadService.findById(1L)).thenReturn(Optional.of(LeadMapper.toDTO(lead)));
        when(contactService.findById(1L)).thenReturn(Optional.empty());

        String viewName = contactController.update(1L, 1L, updatedContact, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Contact not found.");
        assertEquals("redirect:/contacts", viewName);
    }

    @Test
    public void testUpdateContact_LeadNotFound() {
        Contact updatedContact = new Contact();
        updatedContact.setFirstName("Updated");
        updatedContact.setLastName("Contact");

        when(leadService.findById(1L)).thenReturn(Optional.empty());

        String viewName = contactController.update(1L, 1L, updatedContact, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Lead not found.");
        assertEquals("redirect:/contacts", viewName);
    }
}

