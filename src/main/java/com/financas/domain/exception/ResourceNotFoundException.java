package com.financas.domain.exception;

import java.util.UUID;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String resource, UUID id) {
        super(resource + " not found with id: " + id);
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
