package com.isaque.admin.catalogo.domain.validation;

public abstract class Validator {
    private final ValidationHandler handler;

    protected Validator(final ValidationHandler handler) {
        this.handler = handler;
    }

    public abstract void validate();

    protected ValidationHandler getHandler() {
        return this.handler;
    }
}
