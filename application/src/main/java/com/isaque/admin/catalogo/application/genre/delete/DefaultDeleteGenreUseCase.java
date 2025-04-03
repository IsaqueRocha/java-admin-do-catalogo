package com.isaque.admin.catalogo.application.genre.delete;

import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.domain.genre.GenreID;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {
  private final GenreGateway genreGateway;

  public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
    this.genreGateway = Objects.requireNonNull(genreGateway);
  }

  @Override
  public void execute(final String input) {
    this.genreGateway.deleteById(GenreID.from(input));
  }
}
