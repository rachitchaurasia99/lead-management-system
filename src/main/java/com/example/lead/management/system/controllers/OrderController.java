package com.example.lead.management.system.controllers;

import com.example.lead.management.system.dtos.LeadDto;
import com.example.lead.management.system.dtos.OrderDto;
import com.example.lead.management.system.mapper.OrderMapper;
import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.models.Order;
import com.example.lead.management.system.models.User;
import com.example.lead.management.system.repositories.LeadRepository;
import com.example.lead.management.system.services.LeadService;
import com.example.lead.management.system.services.OrderService;
import com.example.lead.management.system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final LeadService leadService;
    private final LeadRepository leadRepository;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, LeadService leadService, LeadRepository leadRepository) {
        this.userService = userService;
        this.orderService = orderService;
        this.leadService = leadService;
        this.leadRepository = leadRepository;
    }
    @GetMapping("/new")
    public String newOrder(Model model) {
        model.addAttribute("order", new OrderDto());
        model.addAttribute("leads", leadService.findAllByUser(currentUser()));
        model.addAttribute("statuses", Order.Status.values());
        return "order-form";
    }

    @PostMapping("/save")
    public String createOrder(Model model, @ModelAttribute OrderDto orderDto) {
        orderService.save(orderDto);
        model.addAttribute("leads", leadService.findAllByUser(currentUser()));
        model.addAttribute("statuses", Order.Status.values());
        model.addAttribute("order", orderDto);
        return "order-form";
    }

    @GetMapping
    public String getOrders(@RequestParam Map<String, String> params, @RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Stream<OrderDto> ordersStream;
        Long leadId = parseLongOrNull(params.get("lead"));

        if (currentUser().getRole().name().equals("ADMIN")) {
            ordersStream = (leadId != null ? orderService.findAllByLeadId(leadId, pageRequest) : orderService.findAll(pageRequest))
                    .stream()
                    .sorted(Comparator.comparing(OrderDto::getCreatedAt));
        } else {
            ordersStream = orderService.findAllByUserId(leadId, currentUser().getId(), pageRequest).stream();
        }

        List<OrderDto> ordersList = ordersStream.toList();

        List<Long> leadIds = ordersList.stream()
                .map(OrderDto::getLeadId)
                .distinct()
                .collect(Collectors.toList());
        List<Lead> leads = leadService.findAllByIds(leadIds);

        Map<Long, LeadDto> leadMap = leads.stream()
                .collect(Collectors.toMap(Lead::getId, lead -> new LeadDto(lead.getId(), lead.getName())));

        ordersList.forEach(orderDto -> {
            LeadDto leadDto = leadMap.get(orderDto.getLeadId());
            if (leadDto != null) {
                orderDto.setLead(leadDto);
            }
        });
model.addAttribute("leads", currentUser().isAdmin() ? leadRepository.findAll() : currentUser().getLeads());
        model.addAttribute("orders", ordersList);
        return "orders";
    }

    @GetMapping("{id}/edit")
    public String getOrder(@PathVariable Long id, Model model) {
        OrderDto order = OrderMapper.toDto(orderService.findById(id));
        if (order == null) {
            model.addAttribute("message", "Order not found");
            return "orders";
        }
        model.addAttribute("order", order);
        model.addAttribute("leads", leadService.findAllByUser(currentUser()));
        model.addAttribute("statuses", Order.Status.values());
        return "order-form";
    }

    @PostMapping("{id}/update")
    public String updateOrder(@PathVariable Long id, Model model, @ModelAttribute OrderDto orderDto) {

        try{
            orderService.update(id, orderDto);
        }
        catch (Exception e){
            model.addAttribute("message", e.fillInStackTrace());
            return "orders";
        }
        model.addAttribute("order", orderDto);
        model.addAttribute("leads", leadService.findAllByUser(currentUser()));
        model.addAttribute("statuses", Order.Status.values());
        return "order-form";
    }

    @PostMapping("{id}/delete")
    public String deleteOrder(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        if (order == null) {
            model.addAttribute("message", "Order not found");
            return "redirect:/orders";
        }
        orderService.delete(order.getId());
        model.addAttribute("leads", leadService.findAllByUser(currentUser()));
        return "redirect:/orders";
    }

    private User currentUser(){
        return userService.currentUser().get();
    }

    private Long parseLongOrNull(String value) {
        try {
            return (value != null && !value.isEmpty()) ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

