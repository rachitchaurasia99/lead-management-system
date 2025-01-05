package com.example.lead.management.system.controllers;

import com.example.lead.management.system.dtos.LeadDto;
import com.example.lead.management.system.mapper.LeadMapper;
import com.example.lead.management.system.mapper.UserMapper;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/leads")
public class LeadController {

    private final LeadService leadService;
    private final UserService userService;

    @Autowired
    public LeadController(LeadService leadService, UserService userService) {
        this.leadService = leadService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("lead", new LeadDto());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("statuses", Lead.Status.values());
        model.addAttribute("current_user", currentUser());
        model.addAttribute("countries", List.of("United States", "Canada", "United Kingdom", "Australia", "India"));
        return "lead-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute LeadDto leadDTO, RedirectAttributes redirectAttributes) {
        Lead lead = new Lead();
        lead.setName(leadDTO.getName());
        lead.setAddress(leadDTO.getAddress());
        lead.setDescription(leadDTO.getDescription());
        lead.setCallFrequency(leadDTO.getCallFrequency());
        lead.setUser(leadDTO.getUser());
        lead.setCurrentStatus(leadDTO.getCurrentStatus());

        leadService.save(lead);
        redirectAttributes.addFlashAttribute("message", "Lead saved successfully.");
        return "redirect:/leads";
    }

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "") String query,
                        Model model) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<LeadDto> leadsPage = currentUser().isAdmin() ?
                (query.isEmpty() ? leadService.findAll(pageRequest) : leadService.findByName(query, pageRequest)) :
                (query.isEmpty() ? leadService.findAllByUser(currentUser(), pageRequest) : leadService.findByUserAndName(currentUser(), query, pageRequest));

        model.addAttribute("current_user", currentUser());
        model.addAttribute("leads", leadsPage);
        model.addAttribute("query", query);
        return "leads";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Lead> leadOpt = currentUser().isAdmin() ? leadService.findById(id).map(LeadMapper::toEntity) :
                                 leadService.findByIdAndUser(id, currentUser());
        if (leadOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Lead not found.");
            return "redirect:/leads";
        }

        Lead lead = leadOpt.get();
        LeadDto leadDTO = LeadMapper.toDTO(lead);
        model.addAttribute("lead", leadDTO);
        model.addAttribute("current_user", currentUser());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("statuses", Lead.Status.values());
        model.addAttribute("countries", List.of("United States", "Canada", "United Kingdom", "Australia", "India"));
        model.addAttribute("allowedTransitions", lead.getStatus().getAllowedTransitions());
        return "lead-form";
    }

    @PostMapping("/{id}/update")
    public String updateLead(@PathVariable Long id, @ModelAttribute LeadDto leadDTO,
                             RedirectAttributes redirectAttributes) {
        try {
            Lead lead = LeadMapper.toEntity(leadDTO);
            leadService.update(id, lead);
            if(lead.getErrors() != null && !lead.getErrors().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", lead.getErrors().get(0));
            }
            else {
                redirectAttributes.addFlashAttribute("message", "Lead updated successfully.");
            }
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/leads/" + id + "/edit";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (leadService.findById(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Lead not found.");
        } else {
            leadService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Lead deleted successfully.");
        }
        return "redirect:/leads";
    }

    public User currentUser(){
        return userService.currentUser().get();
    }
}
