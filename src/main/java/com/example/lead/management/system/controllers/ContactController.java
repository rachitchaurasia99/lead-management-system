package com.example.lead.management.system.controllers;


import com.example.lead.management.system.mapper.LeadMapper;
import com.example.lead.management.system.models.Contact;
import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.services.ContactService;
import com.example.lead.management.system.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/leads/{leadId}/contacts")
public class ContactController {
    private final ContactService contactService;
    private final LeadService leadService;

    @Autowired
    public ContactController(ContactService contactService, LeadService leadService) {
        this.contactService = contactService;
        this.leadService = leadService;
    }

    @GetMapping("/new")
    public String create(@PathVariable Long leadId, Model model) {
        Optional<Lead> lead = leadService.findById(leadId).map(LeadMapper::toEntity);
        if (lead.isEmpty()) {
            throw new RuntimeException("Lead not found");
        }
        Contact contact = new Contact();
        model.addAttribute("contact", contact);
        model.addAttribute("lead", lead.get());
        model.addAttribute("roles", Contact.Enum.values());
        return "contact-form";
    }

    @PostMapping("/save")
    public String save(@PathVariable Long leadId, Contact contact, Model model) {
            Optional<Lead> lead = leadService.findById(leadId).map(LeadMapper::toEntity);
            if (lead.isPresent()) {
                contact.setLead(lead.get());
            } else {
                throw new RuntimeException("Lead not found");
            }
        contactService.save(contact);
        return "redirect:/contacts";
    }

    @GetMapping("")
    public String index(@PathVariable Long leadId, Model model) {
        Optional<Lead> lead = leadService.findById(leadId).map(LeadMapper::toEntity);
        if (lead.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found");
        }
        else{
            model.addAttribute("lead", lead.get());
            model.addAttribute("contacts", contactService.findAllByLead(lead.get()));
        }
        return "contacts";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long leadId, @PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Lead> lead = leadService.findById(leadId).map(LeadMapper::toEntity);
        if (lead.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found");
        }
        else {
            Optional<Contact> contact = contactService.findById(id);

            if (contact.isPresent()) {
                model.addAttribute("contact", contact.get());
                model.addAttribute("lead", lead.get());
                model.addAttribute("roles", Contact.Enum.values());
                return "contact-form";
            } else {
                redirectAttributes.addFlashAttribute("error", "Contact not found.");
                return "redirect:/contacts";
            }
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long leadId, @PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        Optional<Lead> lead = leadService.findById(leadId).map(LeadMapper::toEntity);
        if (lead.isPresent()) {
            Optional<Contact> contact = contactService.findById(id);
            if(contact.isPresent()) {
                model.addAttribute("lead", lead.get());
                contactService.deleteById(contact.get().getId());
            }
            else{
                redirectAttributes.addFlashAttribute("error", "Contact not found.");
            }
        }
        else {
            redirectAttributes.addFlashAttribute("error", "Lead not found.");
        }
        return "contacts";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long leadId, @PathVariable Long id, @ModelAttribute Contact updatedContact, RedirectAttributes redirectAttributes) {
        Optional<Lead> lead = leadService.findById(leadId).map(LeadMapper::toEntity);
        if (lead.isPresent()) {
            Optional<Contact> existingContact = contactService.findById(id);
            if (existingContact.isPresent()) {
                existingContact.get().setFirstName(updatedContact.getFirstName());
                existingContact.get().setLastName(updatedContact.getLastName());
                existingContact.get().setEmail(updatedContact.getEmail());
                existingContact.get().setPhone(updatedContact.getPhone());
                existingContact.get().setRole(updatedContact.getRole());
                contactService.save(existingContact.get());
                redirectAttributes.addFlashAttribute("success", "Contact Updated.");
                return "contact-form";
            }
            else {
                redirectAttributes.addFlashAttribute("error", "Contact not found.");
                return "redirect:/contacts";
            }
        }
        else{
            redirectAttributes.addFlashAttribute("error", "Lead not found.");
            return "redirect:/contacts";
        }
    }
}
