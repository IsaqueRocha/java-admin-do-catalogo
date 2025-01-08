package com.isaque.admin.catalogo.application.category.retrive.list;

import com.isaque.admin.catalogo.application.UseCase;
import com.isaque.admin.catalogo.domain.category.CategorySearchQuery;
import com.isaque.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
