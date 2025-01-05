package com.example.lead.management.system.exceptions;
public class UserHasAssociatedLeadsException extends RuntimeException {
    public UserHasAssociatedLeadsException(String message) {
        super(message);
    }
}

