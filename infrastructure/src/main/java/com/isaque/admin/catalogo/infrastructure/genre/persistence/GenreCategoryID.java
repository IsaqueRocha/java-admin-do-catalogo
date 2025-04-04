package com.isaque.admin.catalogo.infrastructure.genre.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class GenreCategoryID {
  @Column(name = "genre_id", nullable = false)
  private String genreId;
  @Column(name = "category_id", nullable = false)
  private String categoryId;

  public GenreCategoryID() {
  }

  private GenreCategoryID(
      final String genreId,
      final String categoryId
  ) {
    this.genreId = genreId;
    this.categoryId = categoryId;
  }

  public static GenreCategoryID from(
      final String genreId,
      final String categoryId
  ) {
    return new GenreCategoryID(genreId, categoryId);
  }

  public String getGenreId() {
    return genreId;
  }

  public void setGenreId(final String genreId) {
    this.genreId = genreId;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(final String categoryId) {
    this.categoryId = categoryId;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    final GenreCategoryID that = (GenreCategoryID) o;
    return Objects.equals(getGenreId(), that.getGenreId()) && Objects.equals(getCategoryId(), that.getCategoryId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getGenreId(), getCategoryId());
  }
}
