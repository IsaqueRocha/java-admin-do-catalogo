package com.isaque.admin.catalogo.domain.category;

import com.isaque.admin.catalogo.domain.validation.Error;
import com.isaque.admin.catalogo.domain.validation.ValidationHandler;
import com.isaque.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {
    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final Category category;

    protected CategoryValidator(final Category category, final ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();
        if (name == null) {
            this.getHandler().append(new Error("'name' must not be null"));
            return;
        }
        if (name.isBlank()) {
            this.getHandler().append(new Error("'name' must not be empty"));
            return;
        }
        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.getHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
