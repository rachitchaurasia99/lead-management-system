package com.example.lead.management.system.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContactTest {

    private Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact();
    }

    @Test
    void testFirstName() {
        contact.setFirstName("John");
        assertEquals("John", contact.getFirstName());
    }

    @Test
    void testLastName() {
        contact.setLastName("Doe");
        assertEquals("Doe", contact.getLastName());
    }

    @Test
    void testPhone() {
        contact.setPhone("1234567890");
        assertEquals("1234567890", contact.getPhone());
    }

    @Test
    void testEmail() {
        contact.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", contact.getEmail());
    }

    @Test
    void testRole() {
        contact.setRole(Contact.Enum.MANAGER);
        assertEquals(Contact.Enum.MANAGER, contact.getRole());
    }

    @Test
    void testInformation() {
        contact.setInformation("This is additional information.");
        assertEquals("This is additional information.", contact.getInformation());
    }

    @Test
    void testLeadAssociation() {
        Lead lead = mock(Lead.class);
        contact.setLead(lead);
        assertEquals(lead, contact.getLead());
    }

    @Test
    void testCallsAssociation() {
        Call call1 = mock(Call.class);
        Call call2 = mock(Call.class);

        List<Call> calls = new ArrayList<>();
        calls.add(call1);
        calls.add(call2);

        contact.setCalls(calls);
        assertEquals(2, contact.getCalls().size());
        assertTrue(contact.getCalls().contains(call1));
        assertTrue(contact.getCalls().contains(call2));
    }

    @Test
    void testNoArgsConstructor() {
        Contact contact = new Contact();
        assertNotNull(contact);
    }
}
