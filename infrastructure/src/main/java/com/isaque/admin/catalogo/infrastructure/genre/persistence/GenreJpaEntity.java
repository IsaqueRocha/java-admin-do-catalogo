package com.isaque.admin.catalogo.infrastructure.genre.persistence;

import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreID;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "genres")
public class GenreJpaEntity {
  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "active", nullable = false)
  private boolean active;

  @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<GenreCategoryJpaEntity> categories;

  @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant updatedAt;

  @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
  private Instant deletedAt;

  public GenreJpaEntity() {
  }

  private GenreJpaEntity(
      final String id,
      final String name,
      final boolean isActive,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
  ) {
    this.id = id;
    this.name = name;
    this.active = isActive;
    this.categories = new HashSet<>();
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public static GenreJpaEntity from(final Genre genre) {
    final var entity = new GenreJpaEntity(
        genre.getId().getValue(),
        genre.getName(),
        genre.isActive(),
        genre.getCreatedAt(),
        genre.getUpdatedAt(),
        genre.getDeletedAt()
    );

    genre.getCategories().forEach(entity::addCategory);

    return entity;
  }

  public Genre toAggregate() {
    return Genre.with(
        GenreID.from(this.id),
        this.name,
        this.active,
        this.getCategoryIds(),
        this.createdAt,
        this.updatedAt,
        this.deletedAt
    );
  }

  private void addCategory(final CategoryID categoryId) {
    this.categories.add(GenreCategoryJpaEntity.from(this, categoryId));
  }

  private void removeCategory(final CategoryID categoryId) {
    this.categories.remove(GenreCategoryJpaEntity.from(this, categoryId));
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(final boolean active) {
    this.active = active;
  }

  public List<CategoryID> getCategoryIds() {
    return getCategories().stream().map(
        it -> CategoryID.from(it.getId().getCategoryId())
    ).toList();
  }

  public Set<GenreCategoryJpaEntity> getCategories() {
    return categories;
  }

  public void setCategories(final Set<GenreCategoryJpaEntity> categories) {
    this.categories = categories;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(final Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(final Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(final Instant deletedAt) {
    this.deletedAt = deletedAt;
  }
}
