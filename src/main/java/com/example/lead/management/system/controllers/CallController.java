package com.example.lead.management.system.controllers;

import com.example.lead.management.system.models.Call;
import com.example.lead.management.system.services.CallService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calls")
public class CallController {
    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("calls", callService.findAll());
        return "calls";
    }

    @RequestMapping("/new")
    public String create(Model model) {
        model.addAttribute("call", new Call());
        return "call-form";
    }

    @RequestMapping("/save")
    public String save(Call call) {
        callService.save(call);
        return "redirect:/calls";
    }
}
