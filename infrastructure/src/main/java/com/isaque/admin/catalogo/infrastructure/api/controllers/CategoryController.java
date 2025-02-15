package com.isaque.admin.catalogo.infrastructure.api.controllers;

import com.isaque.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.isaque.admin.catalogo.domain.pagination.Pagination;
import com.isaque.admin.catalogo.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CategoryController implements CategoryAPI {
    private final CreateCategoryUseCase useCase;

    public CategoryController(final CreateCategoryUseCase useCase) {
        this.useCase = Objects.requireNonNull(useCase);
    }

    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public Pagination<?> listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return null;
    }
}
