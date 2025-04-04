package com.isaque.admin.catalogo.infrastructure.genre;

import com.isaque.admin.catalogo.MySQLGatewayTest;
import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
class GenreMySQLGatewayTest {
  @Autowired
  private CategoryMySQLGateway categoryGateway;

  @Autowired
  private GenreMySQLGateway genreMySQLGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  void testDependencyInjection() {
    Assertions.assertNotNull(categoryGateway);
    Assertions.assertNotNull(genreMySQLGateway);
    Assertions.assertNotNull(genreRepository);
  }

  @Test
  void givenAValideGenre_whenCallsCreateGenre_thenShouldPersistGenre() {
    final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

    final var expectedName = "Aventura";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId());

    final var genre = Genre.newGenre(expectedName, expectedIsActive);
    genre.addCategories(expectedCategories);

    final var expectedId = genre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    final var actualGenre = genreMySQLGateway.create(genre);

    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIds());
    Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertEquals(genre.getUpdatedAt(), persistedGenre.getUpdatedAt());
    Assertions.assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  void givenAValideGenreWithoutCategories_whenCallsCreateGenre_thenShouldPersistGenre() {
    final var expectedName = "Aventura";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var genre = Genre.newGenre(expectedName, expectedIsActive);

    final var expectedId = genre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    final var actualGenre = genreMySQLGateway.create(genre);

    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIds());
    Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertEquals(genre.getUpdatedAt(), persistedGenre.getUpdatedAt());
    Assertions.assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }
}
