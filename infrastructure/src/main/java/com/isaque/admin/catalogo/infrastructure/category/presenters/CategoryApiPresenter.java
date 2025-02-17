package com.isaque.admin.catalogo.infrastructure.category.presenters;

import com.isaque.admin.catalogo.application.category.retrive.get.CategoryOutput;
import com.isaque.admin.catalogo.infrastructure.category.models.CategoryApiOutput;

import java.util.function.Function;

public interface CategoryApiPresenter {
    static CategoryApiOutput present(final CategoryOutput output) {
        return new CategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }
}
