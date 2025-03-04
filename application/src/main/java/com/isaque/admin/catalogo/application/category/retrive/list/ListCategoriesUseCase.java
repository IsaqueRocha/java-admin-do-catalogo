package com.isaque.admin.catalogo.application.category.retrive.list;

import com.isaque.admin.catalogo.application.UseCase;
import com.isaque.admin.catalogo.domain.pagination.Pagination;
import com.isaque.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
