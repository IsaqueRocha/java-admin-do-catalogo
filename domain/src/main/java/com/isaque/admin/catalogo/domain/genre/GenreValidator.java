package com.isaque.admin.catalogo.domain.genre;

import com.isaque.admin.catalogo.domain.validation.Error;
import com.isaque.admin.catalogo.domain.validation.ValidationHandler;
import com.isaque.admin.catalogo.domain.validation.Validator;

public class GenreValidator extends Validator {
    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;

    private final Genre genre;

    protected GenreValidator(final Genre genre, final ValidationHandler handler) {
        super(handler);
        this.genre = genre;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.genre.getName();
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
            this.getHandler().append(new Error("'name' must be between 1 and 255 characters"));
        }
    }
}
