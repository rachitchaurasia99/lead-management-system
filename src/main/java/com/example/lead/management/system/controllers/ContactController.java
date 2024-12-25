package com.example.lead.management.system.controllers;


import com.example.lead.management.system.models.Contact;
import com.example.lead.management.system.models.Restaurant;
import com.example.lead.management.system.services.ContactService;
import com.example.lead.management.system.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/restaurants/{restaurantId}/contacts")
public class ContactController {
    private final ContactService contactService;
    private final RestaurantService restaurantService;

    @Autowired
    public ContactController(ContactService contactService, RestaurantService restaurantService) {
        this.contactService = contactService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/new")
    public String create(@PathVariable Long restaurantId, Model model) {
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isEmpty()) {
            throw new RuntimeException("Restaurant not found");
        }
        Contact contact = new Contact();
        contact.setRestaurant(restaurant.get());
        model.addAttribute("contact", contact);
        model.addAttribute("restaurant", restaurant.get());
        model.addAttribute("roles", Contact.Enum.values());
        return "contact-form";
    }

    @PostMapping("/save")
    public String save(Contact contact, Model model) {
        contactService.save(contact);
        return "redirect:/contacts";
    }

    @GetMapping("")
    public String index(@PathVariable Long restaurantId, Model model) {
        model.addAttribute("restaurant", restaurantService.findById(restaurantId).get());
        model.addAttribute("contacts", contactService.findAll());
        return "contacts";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Contact> contact = contactService.findById(id);

        if(contact.isPresent()) {
            model.addAttribute("contact", contact.get());
            return "contact-form";
        } else

        {
            redirectAttributes.addFlashAttribute("error", "Contact not found.");
            return "redirect:/contacts";
        }
    }

    @PutMapping("/update")
    public String update(Contact contact, Model model, RedirectAttributes redirectAttributes) {
        contactService.save(contact);
        redirectAttributes.addFlashAttribute("success", "Contact Updated.");
        return "redirect:/contact-form";
    }
}
