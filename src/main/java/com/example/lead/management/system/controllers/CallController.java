package com.example.lead.management.system.controllers;

import com.example.lead.management.system.models.Call;
import com.example.lead.management.system.services.CallService;
import com.example.lead.management.system.services.ContactService;
import com.example.lead.management.system.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/calls")
public class CallController {
    private final CallService callService;
    private final UserService userService;
    private final ContactService contactService;

    public CallController(CallService callService, UserService userService, ContactService contactService) {
        this.callService = callService;
        this.userService = userService;
        this.contactService = contactService;
    }

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Call> callsPage;
        callsPage = callService.findAllByTime(pageRequest);
        model.addAttribute("calls", callsPage);
        return "calls";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("call", new Call());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("contacts", contactService.findAll());
        return "call-form";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Call> call = callService.findById(id);

        if(call.isPresent()) {
            model.addAttribute("call", call.get());
            model.addAttribute("users", userService.findAll());
            model.addAttribute("contacts", contactService.findAll());
            return "call-form";
        } else

        {
            redirectAttributes.addFlashAttribute("error", "Call not found.");
            return "redirect:/calls";
        }
    }

    @PostMapping("/save")
    public String save(Call call) {
        callService.save(call);
        return "call-form";
    }
}
