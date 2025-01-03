package com.isaque.admin.catalogo.application.category.create;

import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand command) {
        final var name = command.name();
        final var description = command.description();
        final var isActive = command.isActive();

        final Category category = Category.newCategory(name, description, isActive);
        category.validate(new ThrowsValidationHandler());

        return CreateCategoryOutput.from(this.categoryGateway.create(category));
    }
}
