package com.isaque.admin.catalogo.application.category.retrive.get;

import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.exceptions.DomainException;
import com.isaque.admin.catalogo.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    private static Supplier<DomainException> notFound(CategoryID id) {
        return () -> DomainException.with(new Error("Category with id %s was not found".formatted(id.getValue())));
    }

    @Override
    public CategoryOutput execute(final String input) {
        final var categoryId = CategoryID.from(input);
        return this.categoryGateway.findById(categoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(categoryId));
    }
}
