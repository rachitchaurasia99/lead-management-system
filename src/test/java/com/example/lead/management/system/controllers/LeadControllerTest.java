package com.example.lead.management.system.controllers;

import com.example.lead.management.system.dtos.LeadDto;
import com.example.lead.management.system.mapper.LeadMapper;
import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.models.User;
import com.example.lead.management.system.services.LeadService;
import com.example.lead.management.system.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LeadControllerTest {

    @InjectMocks
    private LeadController leadController;

    @Mock
    private LeadService leadService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Page<LeadDto> leadsPage;

    @Mock
    private User currentUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userService.currentUser()).thenReturn(Optional.of(currentUser));
    }
    User currentUser() {
        return new User();
    }

    @Test
    void testCreate() {
        when(userService.findAll()).thenReturn(List.of(new User())); // Mock users
        when(currentUser.isAdmin()).thenReturn(true); // Mock admin check

        String viewName = leadController.create(model);

        verify(model).addAttribute(eq("lead"), any(LeadDto.class));
        verify(model).addAttribute(eq("users"), anyList());
        verify(model).addAttribute(eq("statuses"), eq(Lead.Status.values()));
        verify(model).addAttribute(eq("current_user"), eq(currentUser));
        verify(model).addAttribute(eq("countries"), anyList());

        assertEquals("lead-form", viewName);
    }

    @Test
    void testSave() {
        LeadDto leadDto = new LeadDto();
        leadDto.setName("Test Lead");
        leadDto.setAddress("123 Test Street");
        leadDto.setDescription("Test description");
        leadDto.setCallFrequency(5);
        leadDto.setUser(new User());
        leadDto.setCurrentStatus(Lead.Status.NEW);

        String viewName = leadController.save(leadDto, redirectAttributes);

        verify(leadService).save(any(Lead.class));
        verify(redirectAttributes).addFlashAttribute("success", "Lead saved successfully.");
        assertEquals("redirect:/leads", viewName);
    }

    @Test
    void testDeleteLeadNotFound() {
        Long leadId = 1L;
        when(leadService.findById(leadId)).thenReturn(Optional.empty());

        String viewName = leadController.delete(leadId, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Lead not found.");
        assertEquals("redirect:/leads", viewName);
    }

    @Test
    void testDeleteLeadFound() {
        Long leadId = 1L;
        when(leadService.findById(leadId)).thenReturn(Optional.of(new LeadDto()));

        String viewName = leadController.delete(leadId, redirectAttributes);

        verify(leadService).deleteById(eq(leadId));
        verify(redirectAttributes).addFlashAttribute("success", "Lead deleted successfully.");
        assertEquals("redirect:/leads", viewName);
    }
}

