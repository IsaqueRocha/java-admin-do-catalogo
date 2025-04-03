package com.isaque.admin.catalogo.application.genre.retrieve.list;

import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
    String name,
    boolean isActive,
    List<String> categories,
    Instant createdAt,
    Instant deletedAt
) {
  public static GenreListOutput from(final Genre genre) {
    return new GenreListOutput(
        genre.getName(),
        genre.isActive(),
        genre.getCategories().stream().map(CategoryID::getValue).toList(),
        genre.getCreatedAt(),
        genre.getDeletedAt());
  }
}
