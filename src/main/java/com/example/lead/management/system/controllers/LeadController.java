package com.example.lead.management.system.controllers;

import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.models.User;
import com.example.lead.management.system.services.LeadService;
import com.example.lead.management.system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/leads")
public class LeadController  {
    private final LeadService leadService;
    private final UserService userService;

    @Autowired
    public LeadController(LeadService leadService, UserService userService) {

        this.leadService = leadService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("lead", new Lead());
        model.addAttribute("statuses", Lead.Status.values());
        return "lead-form";
    }

    @PostMapping("/save")
    public String save(Lead lead, RedirectAttributes redirectAttributes) {
        leadService.save(lead);
        redirectAttributes.addFlashAttribute("success", "Lead saved successfully.");
        return "redirect:/leads";
    }

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "") String query,
                        Model model) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Lead> leadsPage = query.isEmpty() ?
                leadService.findAll(pageRequest) :
                leadService.findByNameContaining(query, pageRequest);

        model.addAttribute("current_user", userService.currentUser().get());
        model.addAttribute("leads", leadsPage);
        model.addAttribute("query", query);
        return "leads";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Lead> leadOpt = leadService.findById(id);
        if (leadOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Lead not found.");
            return "redirect:/leads";
        }

        Lead lead = leadOpt.get();
        model.addAttribute("lead", lead);
        model.addAttribute("statuses", Lead.Status.values());
        model.addAttribute("allowedTransitions", lead.getStatus().getAllowedTransitions());
        return "lead-form";
    }

    @PostMapping("/{id}/update")
    public String updateLead(@PathVariable Long id, @ModelAttribute Lead updatedLead,
                             RedirectAttributes redirectAttributes) {
        Optional<Lead> existingLeadOpt = leadService.findById(id);
        if (existingLeadOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Lead not found.");
            return "redirect:/leads";
        }

        Lead existingLead = existingLeadOpt.get();
        try {
            // Copy updated fields
            existingLead.setName(updatedLead.getName());
            existingLead.setAddress(updatedLead.getAddress());
            existingLead.setDescription(updatedLead.getDescription());
            existingLead.setCallFrequency(updatedLead.getCallFrequency());

            // Handle status transition
            if (updatedLead.getStatus() != existingLead.getStatus()) {
                existingLead.transitionStatus(updatedLead.getStatus());
            }

            leadService.update(existingLead);
            redirectAttributes.addFlashAttribute("success", "Lead updated successfully.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/leads/" + id + "/edit";
        }

        return "redirect:/leads";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (leadService.findById(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Lead not found.");
        } else {
            leadService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Lead deleted successfully.");
        }
        return "redirect:/leads";
    }
}
