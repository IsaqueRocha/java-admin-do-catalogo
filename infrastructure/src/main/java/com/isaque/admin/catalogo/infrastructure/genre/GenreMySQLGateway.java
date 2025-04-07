package com.isaque.admin.catalogo.infrastructure.genre;

import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.domain.genre.GenreID;
import com.isaque.admin.catalogo.domain.pagination.Pagination;
import com.isaque.admin.catalogo.domain.pagination.SearchQuery;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import com.isaque.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

  @Override
  public void deleteById(final GenreID id) {
    final var genreId = id.getValue();
    if (this.repository.existsById(genreId)) {
      this.repository.deleteById(id.getValue());
    }
  }

  @Override
  public Optional<Genre> findById(final GenreID id) {
    return this.repository.findById(id.getValue()).map(GenreJpaEntity::toAggregate);
  }

  @Override
  public Genre update(final Genre genre) {
    return save(genre);
  }

  @Override
  public Pagination<Genre> findAll(final SearchQuery query) {
    final var page = PageRequest.of(
        query.page(),
        query.perPage(),
        Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
    );
    
    final var where = Optional.ofNullable(query.terms())
        .filter(str -> !str.isBlank())
        .map(this::assembleSpecification)
        .orElse(null);

    final var pageResult = this.repository.findAll(Specification.where(where), page);

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(GenreJpaEntity::toAggregate).toList()
    );
  }

  private Genre save(final Genre genre) {
    return this.repository.save(GenreJpaEntity.from(genre)).toAggregate();
  }

  private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
    return SpecificationUtils.like("name", terms);
  }
}
