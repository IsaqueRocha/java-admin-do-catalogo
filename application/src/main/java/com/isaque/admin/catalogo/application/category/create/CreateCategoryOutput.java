package com.isaque.admin.catalogo.application.category.create;

import com.isaque.admin.catalogo.domain.category.Category;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId().getValue());
    }
    public static CreateCategoryOutput from(final String id) {
        return new CreateCategoryOutput(id);
    }
}
