package com.isaque.admin.catalogo.application.genre.retrieve.list;

import com.isaque.admin.catalogo.application.UseCase;
import com.isaque.admin.catalogo.domain.pagination.Pagination;
import com.isaque.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
