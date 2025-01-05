package com.example.lead.management.system.controllers;

import com.example.lead.management.system.dtos.UserDto;
import com.example.lead.management.system.exceptions.UserHasAssociatedLeadsException;
import com.example.lead.management.system.models.User;
import com.example.lead.management.system.services.UserService;
import com.example.lead.management.system.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Time;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        Set<String> timeZones = ZoneId.getAvailableZoneIds();
        model.addAttribute("timeZones", timeZones);
        model.addAttribute("user", new UserDto());
        model.addAttribute("roles", User.Role.values());
        return "user-form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<UserDto> userDto = userService.findById(id).map(UserMapper::toDto);
        if (userDto.isPresent()) {
            Set<String> timeZones = ZoneId.getAvailableZoneIds();
            model.addAttribute("timeZones", timeZones);
            model.addAttribute("user", userDto.get());
            model.addAttribute("roles", User.Role.values());
            return "user-form";
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/users";
        }
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute UserDto userDto, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.update(id, userDto);
            model.addAttribute("user", userDto);
            redirectAttributes.addFlashAttribute("message", "User updated successfully");
            return "user-form";
        } catch (NoSuchElementException e) {
            model.addAttribute("error", "User not found");
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/users";
        }
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {
        try {
            userDto.setRole("USER");
            userDto.setJoinDate(new Time(System.currentTimeMillis()));
            userService.save(UserMapper.toEntity(userDto));
            return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public String showAllUsers(@RequestParam Map<String, String> params, Model model) {
        String performance = params.get("performance");
        List<UserDto> users;

        if (performance != null) {
            switch (performance) {
                case "topperforming":
                    users = userService.sortByTopPerformer().stream().map(UserMapper::toDto).toList();
                    model.addAttribute("selectedFilter", "topperforming");
                    break;
                case "lowestperforming":
                    users = userService.sortByBottomPerformer().stream().map(UserMapper::toDto).toList();
                    model.addAttribute("selectedFilter", "lowestperforming");
                    break;
                default:
                    users = userService.findAll().stream().map(UserMapper::toDto).toList();
                    model.addAttribute("selectedFilter", "");
            }
        } else {
            users = userService.findAll().stream().map(UserMapper::toDto).toList();
            model.addAttribute("selectedFilter", "");
        }

        model.addAttribute("users", users);
        return "users";
    }
    @PostMapping("{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id).orElseThrow(NoSuchElementException::new);
            userService.delete(user);
            redirectAttributes.addFlashAttribute("message", "User removed successfully");
        } catch (UserHasAssociatedLeadsException e) {
            redirectAttributes.addFlashAttribute("error", "User has associated leads");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users";
    }

}
