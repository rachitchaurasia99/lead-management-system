package com.example.lead.management.system.controllers;

import com.example.lead.management.system.models.Contact;
import com.example.lead.management.system.models.Restaurant;
import com.example.lead.management.system.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        model.addAttribute("status", Restaurant.Status.values());
        return "restaurant-form";
    }

    @PostMapping("/save")
    public String save(Restaurant restaurant) {
        restaurantService.save(restaurant);
        return "redirect:/restaurants";
    }

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "") String query,
                        Model model) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Restaurant> restaurantsPage;
        System.out.println("debugger3");
        if (query.isEmpty()) {
            restaurantsPage = restaurantService.findAll(pageRequest);
        } else {
            System.out.println("debugger1");
            restaurantsPage = restaurantService.findByNameContaining(query, pageRequest);
        }

        model.addAttribute("restaurants", restaurantsPage);
        model.addAttribute("query", query);

        return "restaurants";
    }


    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Restaurant> restaurant = restaurantService.findById(id);

        if(restaurant.isPresent()) {
            model.addAttribute("restaurant", restaurant.get());
            return "restaurant-form";
        } else

        {
            redirectAttributes.addFlashAttribute("error", "Restaurant not found.");
            return "redirect:/restaurants";
        }
    }

    @PostMapping("/{id}/update")
    public String updateRestaurant(Restaurant restaurant, @PathVariable Long id, Model model) {
        Optional<Restaurant> existingRestaurant = restaurantService.findById(id);

        if(existingRestaurant.isPresent()) {
            existingRestaurant.get().setName(restaurant.getName());
            existingRestaurant.get().setAddress(restaurant.getAddress());
            existingRestaurant.get().setDescription(restaurant.getDescription());
            existingRestaurant.get().setStatus(restaurant.getStatus());
            restaurantService.update(existingRestaurant.get());

            model.addAttribute("restaurant", existingRestaurant.get());
            model.addAttribute("status", Restaurant.Status.values());
        }
        return "restaurant-form";

    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Restaurant> restaurant = restaurantService.findById(id);

        if(restaurant.isPresent()) {
            restaurantService.deleteById(restaurant.get().getId());
            return "redirect:/restaurants";
        } else

        {
            redirectAttributes.addFlashAttribute("error", "Restaurant not found.");
            return "redirect:/restaurants";
        }
    }
}
