package com.isaque.admin.catalogo.infrastructure.genre.persistence;

import com.isaque.admin.catalogo.domain.category.CategoryID;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "genres_categories")
public class GenreCategoryJpaEntity {
  @EmbeddedId
  private GenreCategoryID id;

  @ManyToOne
  @MapsId("genreId")
  private GenreJpaEntity genre;

  public GenreCategoryJpaEntity() {
  }

  private GenreCategoryJpaEntity(
      final GenreJpaEntity genre,
      final CategoryID categoryId
  ) {
    this.id = GenreCategoryID.from(genre.getId(), categoryId.getValue());
    this.genre = genre;
  }

  public static GenreCategoryJpaEntity from(final GenreJpaEntity genre, final CategoryID categoryId) {
    return new GenreCategoryJpaEntity(genre, categoryId);
  }

  public GenreCategoryID getId() {
    return id;
  }

  public void setId(final GenreCategoryID id) {
    this.id = id;
  }

  public GenreJpaEntity getGenre() {
    return genre;
  }

  public void setGenre(final GenreJpaEntity genre) {
    this.genre = genre;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    final GenreCategoryJpaEntity that = (GenreCategoryJpaEntity) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
