package com.isaque.admin.catalogo.application.genre.retrieve.get;

import com.isaque.admin.catalogo.domain.exceptions.NotFoundException;
import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.domain.genre.GenreID;

import java.util.Objects;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {
  private final GenreGateway genreGateway;

  public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
    this.genreGateway = Objects.requireNonNull(genreGateway);
  }

  @Override
  public GenreOutput execute(final String input) {
    final var genreId = GenreID.from(input);
    return this.genreGateway.findById(genreId)
        .map(GenreOutput::from)
        .orElseThrow(() -> NotFoundException.with(Genre.class, genreId));
  }
}
