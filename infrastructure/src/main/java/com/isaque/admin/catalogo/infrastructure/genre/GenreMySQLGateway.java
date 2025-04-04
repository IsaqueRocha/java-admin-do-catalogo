package com.isaque.admin.catalogo.infrastructure.genre;

import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.domain.genre.GenreID;
import com.isaque.admin.catalogo.domain.pagination.Pagination;
import com.isaque.admin.catalogo.domain.pagination.SearchQuery;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {
  private final GenreRepository repository;

  public GenreMySQLGateway(final GenreRepository genreRepository) {
    this.repository = Objects.requireNonNull(genreRepository);
  }

  @Override
  public Genre create(final Genre genre) {
    return save(genre);
  }

  private Genre save(final Genre genre) {
    return this.repository.save(GenreJpaEntity.from(genre)).toAggregate();
  }

  @Override
  public void deleteById(final GenreID id) {

  }

  @Override
  public Optional<Genre> findById(final GenreID id) {
    return Optional.empty();
  }

  @Override
  public Genre update(final Genre genre) {
    return null;
  }

  @Override
  public Pagination<Genre> findAll(final SearchQuery query) {
    return null;
  }
}
