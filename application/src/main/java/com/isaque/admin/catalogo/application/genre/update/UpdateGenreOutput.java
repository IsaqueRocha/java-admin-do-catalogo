package com.isaque.admin.catalogo.application.genre.update;

import com.isaque.admin.catalogo.domain.genre.Genre;

public record UpdateGenreOutput(String id) {
  public static UpdateGenreOutput from(final Genre genre) {
    return new UpdateGenreOutput(genre.getId().getValue());
  }
}
