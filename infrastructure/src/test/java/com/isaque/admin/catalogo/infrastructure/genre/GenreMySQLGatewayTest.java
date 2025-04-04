package com.isaque.admin.catalogo.infrastructure.genre;

import com.isaque.admin.catalogo.MySQLGatewayTest;
import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreID;
import com.isaque.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

@MySQLGatewayTest
class GenreMySQLGatewayTest {
  @Autowired
  private CategoryMySQLGateway categoryGateway;

  @Autowired
  private GenreMySQLGateway genreGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  void testDependencyInjection() {
    Assertions.assertNotNull(categoryGateway);
    Assertions.assertNotNull(genreGateway);
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

    final var actualGenre = genreGateway.create(genre);

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

    final var actualGenre = genreGateway.create(genre);

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
  void givenAValideGenreWithoutCategories_whenCallsUpdateGenreWithCategories_thenShouldPersistGenre() {
    final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
    final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

    final var expectedName = "Aventura";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId(), series.getId());

    final var genre = Genre.newGenre("av", expectedIsActive);

    final var expectedId = genre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

    Assertions.assertEquals("av", genre.getName());
    Assertions.assertEquals(0, genre.getCategories().size());

    final var actualGenre = genreGateway.update(
        Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories)
    );

    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
    Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIds()));
    Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  void givenAValideGenreWithoutCategories_whenCallsUpdateGenreCleaningCategories_thenShouldPersistGenre() {
    // given
    final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
    final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

    final var expectedName = "Aventura";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var genre = Genre.newGenre("av", expectedIsActive);
    genre.addCategories(List.of(filmes.getId(), series.getId()));

    final var expectedId = genre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

    Assertions.assertEquals("av", genre.getName());
    Assertions.assertEquals(2, genre.getCategories().size());

    final var actualGenre = genreGateway.update(
        Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories)
    );

    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIds());
    Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }


  @Test
  void givenAValideGenreInactive_whenCallsUpdateGenreActivating_thenShouldPersistGenre() {
    final var expectedName = "Aventura";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var genre = Genre.newGenre(expectedName, false);

    final var expectedId = genre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

    Assertions.assertFalse(genre.isActive());
    Assertions.assertNotNull(genre.getDeletedAt());

    final var actualGenre = genreGateway.update(
        Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories)
    );

    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIds());
    Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  void givenAValideGenreActive_whenCallsUpdateGenreInactivating_thenShouldPersistGenre() {
    final var expectedName = "Aventura";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var genre = Genre.newGenre(expectedName, true);

    final var expectedId = genre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

    Assertions.assertTrue(genre.isActive());
    Assertions.assertNull(genre.getDeletedAt());

    final var actualGenre = genreGateway.update(
        Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories)
    );

    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNotNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIds());
    Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertNotNull(persistedGenre.getDeletedAt());
  }

  @Test
  void givenAPrePersistedGenre_whenCallsDeleteById_thenShouldDeleteGenre() {
    //given
    final var genre = Genre.newGenre("Aventura", true);

    genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

    Assertions.assertEquals(1, genreRepository.count());

    // when
    genreGateway.deleteById(genre.getId());

    //then
    Assertions.assertEquals(0, genreRepository.count());
  }

  @Test
  void givenAInvalidGenre_whenCallsDeleteById_thenShouldDeleteGenre() {
    //given
    Assertions.assertEquals(0, genreRepository.count());

    // when
    genreGateway.deleteById(GenreID.from("invalid"));

    //then
    Assertions.assertEquals(0, genreRepository.count());
  }

  @Test
  void givenAPrePersistedGenre_whenCallsFindById_thenShouldReturnGenre() {
    // given
    final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
    final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

    final var expectedName = "Aventura";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId(), series.getId());

    final var genre = Genre.newGenre(expectedName, expectedIsActive);
    genre.addCategories(expectedCategories);

    final var expectedId = genre.getId();

    genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

    Assertions.assertEquals(1, genreRepository.count());

    // when
    final var actualGenre = genreGateway.findById(expectedId).get();

    //then

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAnInvalidGenre_whenCallsFindById_thenShouldReturnEmpty() {
    // given
    final var expectedId = GenreID.from("123");

    Assertions.assertEquals(0, genreRepository.count());

    // when
    final var actualGenre = genreGateway.findById(expectedId);

    //then
    Assertions.assertTrue(actualGenre.isEmpty());
  }

  private List<CategoryID> sorted(final List<CategoryID> expectedCategories) {
    return expectedCategories.stream()
        .sorted(Comparator.comparing(CategoryID::getValue))
        .toList();
  }

}
