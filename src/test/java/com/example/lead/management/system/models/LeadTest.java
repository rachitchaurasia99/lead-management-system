package com.example.lead.management.system.models;

import com.example.lead.management.system.services.CallService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.util.*;

class LeadTest {

    private Lead lead;

    @BeforeEach
    void setUp() {
        lead = new Lead();
        lead.setStatus(Lead.Status.NEW);

    }

    @Test
    void testInitialStatus() {
        Assertions.assertEquals(Lead.Status.NEW, lead.getStatus(), "Default status should be NEW");
    }

    @Test
    void testAllowedTransitionsFromNew() {
        Set<Lead.Status> allowedTransitions = lead.getAllowedStatuses();
        Assertions.assertTrue(allowedTransitions.contains(Lead.Status.CONTACTED),
                "NEW should transition to CONTACTED");
        Assertions.assertFalse(allowedTransitions.contains(Lead.Status.LOST),
                "NEW should not transition directly to LOST");
    }

    @Test
    void testTransitionToValidStatus() {
        lead.transitionStatus(Lead.Status.CONTACTED);
        Assertions.assertEquals(Lead.Status.CONTACTED, lead.getStatus(),
                "Should successfully transition to CONTACTED");
    }

    @Test
    void testTransitionToInvalidStatusThrowsException() {
        Assertions.assertThrows(IllegalStateException.class,
                () -> lead.transitionStatus(Lead.Status.LOST),
                "Transitioning from NEW to LOST should throw an exception");
    }

    @Test
    void testLastCallReturnsLatestCompletedDate() {
        Contact contact1 = new Contact();
        Call call1 = new Call();
        call1.setDateTime(new Date(1000));
        call1.setStatus(Call.Status.IN_PROGRESS);
        Call call2 = new Call();
        call2.setDateTime(new Date(2000));
        call2.setStatus(Call.Status.COMPLETED);
        contact1.setCalls(List.of(call1, call2));

        Contact contact2 = new Contact();
        Call call3 = new Call();
        call3.setDateTime(new Date(3000));
        call3.setStatus(Call.Status.COMPLETED);
        contact2.setCalls(List.of(call3));

        lead.setContacts(List.of(contact2, contact1));

        Assertions.assertEquals(new Date(3000), lead.lastCall(),
                "Last call should return the most recent completed call date");
    }

    @Test
    void testLastCallReturnsNullWhenNoCompletedCalls() {
        // Create calls for the first contact
        Contact contact1 = new Contact();
        Call call1 = new Call();
        call1.setDateTime(new Date(1000));
        call1.setStatus(Call.Status.IN_PROGRESS);
        Call call2 = new Call();
        call2.setDateTime(new Date(2000));
        call2.setStatus(Call.Status.IN_PROGRESS);
        contact1.setCalls(List.of(call1, call2));

        lead.setContacts(List.of(contact1));

        Assertions.assertNull(lead.lastCall(), "Last call should return null when no completed calls exist");
    }


    @Test
    void testLastCallWhenNoCalls() {
        lead.setContacts(Collections.emptyList());
        Assertions.assertNull(lead.lastCall(), "Last call should be null when no calls exist");
    }

    @Test
    void testGetAllowedStatuses() {
        lead.setStatus(Lead.Status.QUALIFIED);
        Set<Lead.Status> allowedStatuses = lead.getAllowedStatuses();
        Assertions.assertTrue(allowedStatuses.contains(Lead.Status.IN_PROGRESS),
                "QUALIFIED should allow transition to IN_PROGRESS");
        Assertions.assertFalse(allowedStatuses.contains(Lead.Status.CONVERTED),
                "QUALIFIED should not allow direct transition to CONVERTED");
    }

    @Test
    void testSetAndGetAttributes() {
        lead.setName("John Doe");
        lead.setAddress("123 Main Street");
        lead.setDescription("Important Lead");
        Assertions.assertEquals("John Doe", lead.getName());
        Assertions.assertEquals("123 Main Street", lead.getAddress());
        Assertions.assertEquals("Important Lead", lead.getDescription());
    }
}
