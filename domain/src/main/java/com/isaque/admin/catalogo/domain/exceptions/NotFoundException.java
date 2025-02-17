package com.isaque.admin.catalogo.domain.exceptions;

import com.isaque.admin.catalogo.domain.AggregateRoot;
import com.isaque.admin.catalogo.domain.Identifier;
import com.isaque.admin.catalogo.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot> aggregate,
            final Identifier id
    ) {
        final var error = "%s with id %s was not found".formatted(aggregate.getSimpleName(), id.getValue());
        return new NotFoundException(error, Collections.emptyList());
    }
}
