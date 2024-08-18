package com.isaque.admin.catalogo.domain.category;

import com.isaque.admin.catalogo.domain.validation.Error;
import com.isaque.admin.catalogo.domain.validation.ValidationHandler;
import com.isaque.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private final Category category;

    protected CategoryValidator(final Category category, final ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        if (category.getName() == null) {
            this.getHandler().append(new Error("'name' must not be null"));
        }
    }
}
