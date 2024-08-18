package com.isaque.admin.catalogo.domain.validation.handler;

import com.isaque.admin.catalogo.domain.exceptions.DomainException;
import com.isaque.admin.catalogo.domain.validation.Error;
import com.isaque.admin.catalogo.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error error) {
        throw new DomainException(List.of(error));
    }

    @Override
    public ValidationHandler append(final ValidationHandler handler) {
        throw new DomainException(handler.getErrors());
    }

    @Override
    public ValidationHandler validate(Validation validation) {
        try {
            validation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(List.of(new Error(ex.getMessage())));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return null;
    }

    @Override
    public boolean hasError() {
        return ValidationHandler.super.hasError();
    }
}
