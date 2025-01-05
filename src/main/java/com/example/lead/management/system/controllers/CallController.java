package com.example.lead.management.system.controllers;

import com.example.lead.management.system.dtos.CallDto;
import com.example.lead.management.system.mapper.CallMapper;
import com.example.lead.management.system.models.User;
import com.example.lead.management.system.services.CallService;
import com.example.lead.management.system.services.ContactService;
import com.example.lead.management.system.services.LeadService;
import com.example.lead.management.system.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/calls")
public class CallController {

    private final CallService callService;
    private final UserService userService;
    private final ContactService contactService;
    private final LeadService leadService;
    private final CallMapper callMapper;

    public CallController(CallService callService, UserService userService, ContactService contactService,
                          LeadService leadService, CallMapper callMapper) {
        this.callService = callService;
        this.userService = userService;
        this.contactService = contactService;
        this.leadService = leadService;
        this.callMapper = callMapper;
    }

    @GetMapping("")
    public String index(@RequestParam Map<String, String> params,
                        @RequestParam(defaultValue = "0") int page,
                        Model model) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        Long userId = parseLongOrNull(params.get("user"));
        Long leadId = parseLongOrNull(params.get("lead"));

        Page<CallDto> callsPage = callService.findAllByTime(leadId, userId, pageRequest);
        if(callsPage.hasContent()) {
            model.addAttribute("calls", callsPage);
            model.addAttribute("leads", leadService.findAll());
            model.addAttribute("users", userService.findAll());
        }
        else{
            model.addAttribute("calls", callsPage);
            model.addAttribute("message", "No Calls Found");
        }
        model.addAttribute("currentUser", userService.currentUser());
        return "calls";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("call", new CallDto());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("contacts", contactService.findAllByUser(currentUser()));
        return "call-form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return callService.findById(id)
                .map(call -> {
                    model.addAttribute("call", callMapper.toDto(call));
                    model.addAttribute("users", userService.findAll());
                    model.addAttribute("contacts", contactService.findAllByUser(currentUser()));
                    return "call-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Call not found.");
                    return "redirect:/calls";
                });
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CallDto callDto) {
        callDto.setUserId(currentUser().getId());
        callService.save(callDto);
        return "redirect:/calls";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute CallDto callDto, Model model, RedirectAttributes redirectAttributes) {
        try {
            callService.update(id, callDto);
            redirectAttributes.addFlashAttribute("message", "Call updated successfully.");
            model.addAttribute("call", callDto);
            return "call-form";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Call not found.");
            model.addAttribute("calls", callService.findAllByTime(null, null, PageRequest.of(0, 10)));
            return "redirect:/calls";
        }
    }

    private User currentUser() {
        return userService.currentUser()
                .orElseThrow(() -> new IllegalStateException("Current user not found"));
    }

    private Long parseLongOrNull(String value) {
        try {
            return (value != null && !value.isEmpty()) ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
