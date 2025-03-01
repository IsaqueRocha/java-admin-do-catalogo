package com.isaque.admin.catalogo.infrastructure.category.presenters;

import com.isaque.admin.catalogo.application.category.retrive.get.CategoryOutput;
import com.isaque.admin.catalogo.application.category.retrive.list.CategoryListOutput;
import com.isaque.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.isaque.admin.catalogo.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {
    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
