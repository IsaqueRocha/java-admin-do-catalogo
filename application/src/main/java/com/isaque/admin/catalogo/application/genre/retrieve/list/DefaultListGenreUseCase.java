package com.isaque.admin.catalogo.application.genre.retrieve.list;

import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.domain.pagination.Pagination;
import com.isaque.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListGenreUseCase extends ListGenreUseCase {
  private final GenreGateway genreGateway;

  public DefaultListGenreUseCase(final GenreGateway genreGateway) {
    this.genreGateway = Objects.requireNonNull(genreGateway);
  }

  @Override
  public Pagination<GenreListOutput> execute(final SearchQuery query) {
    return this.genreGateway.findAll(query).map(GenreListOutput::from);
  }
}
