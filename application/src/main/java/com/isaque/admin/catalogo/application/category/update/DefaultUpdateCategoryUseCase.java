package com.isaque.admin.catalogo.application.category.update;

import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.exceptions.DomainException;
import com.isaque.admin.catalogo.domain.exceptions.NotFoundException;
import com.isaque.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    private static Supplier<DomainException> notFound(CategoryID id) {
        return () -> NotFoundException.with(Category.class, id);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand input) {
        final var id = CategoryID.from(input.id());
        final var name = input.name();
        final var description = input.description();
        final var isActive = input.isActive();

        final var category = this.categoryGateway.findById(id).orElseThrow(notFound(id));

        final var notification = Notification.create();

        category.update(name, description, isActive).validate(notification);

        return notification.hasError() ? API.Left(notification) : update(category);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category category) {
        return API.Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
