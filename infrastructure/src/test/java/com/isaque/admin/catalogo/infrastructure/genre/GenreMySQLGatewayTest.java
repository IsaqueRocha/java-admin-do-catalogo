package com.isaque.admin.catalogo.infrastructure.genre;

import com.isaque.admin.catalogo.MySQLGatewayTest;
import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreID;
import com.isaque.admin.catalogo.domain.pagination.SearchQuery;
import com.isaque.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
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

  @Test
  void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTerms = "";
    final var expectedSort = "name";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var searchQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = genreGateway.findAll(searchQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedTotal, actualPage.items().size());
  }

  @ParameterizedTest
  @CsvSource({
      "aç,0,10,1,1,Ação",
      "dr,0,10,1,1,Drama",
      "com,0,10,1,1,Comédia romântica",
      "cien,0,10,1,1,Ficção científica",
      "terr,0,10,1,1,Terror",
  })
  void givenAValidTerm_whenCallsFindAll_thenShouldReturnFiltered(
      final String expectedTerms,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenreName
  ) {
    // given
    mockGenres();
    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var searchQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = genreGateway.findAll(searchQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
    Assertions.assertEquals(expectedGenreName, actualPage.items().getFirst().getName());
  }

  @ParameterizedTest
  @CsvSource({
      "name,asc,0,10,5,5,Ação",
      "name,desc,0,10,5,5,Terror",
      "createdAt,asc,0,10,5,5,Comédia romântica",
      "createdAt,desc,0,10,5,5,Ficção científica",
  })
  void givenAValidSortAndDirection_whenCallsFindAll_thenShouldReturnFiltered(
      final String expectedSort,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenreName
  ) {
    // given
    mockGenres();
    final var expectedTerms = "";


    final var searchQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = genreGateway.findAll(searchQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
    Assertions.assertEquals(expectedGenreName, actualPage.items().getFirst().getName());
  }

  private void mockGenres() {
    genreRepository.saveAllAndFlush(List.of(
        GenreJpaEntity.from(Genre.newGenre("Comédia romântica", true)),
        GenreJpaEntity.from(Genre.newGenre("Ação", true)),
        GenreJpaEntity.from(Genre.newGenre("Drama", true)),
        GenreJpaEntity.from(Genre.newGenre("Terror", true)),
        GenreJpaEntity.from(Genre.newGenre("Ficção científica", true))
    ));
  }

  @ParameterizedTest
  @CsvSource({
      "0,2,2,5,Ação;Comédia romântica",
      "1,2,2,5,Drama;Ficção científica",
      "2,2,1,5,Terror",
  })
  void givenAValidPaging_whenCallsFindAll_thenShouldReturnPaged(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenres
  ) {
    // given
    mockGenres();
    final var expectedTerms = "";
    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var searchQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = genreGateway.findAll(searchQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

    int index = 0;
    for (final var expectedName : expectedGenres.split(";")) {
      final var actualName = actualPage.items().get(index).getName();
      Assertions.assertEquals(expectedName, actualName);
      index++;
    }
  }

  private List<CategoryID> sorted(final List<CategoryID> expectedCategories) {
    return expectedCategories.stream()
        .sorted(Comparator.comparing(CategoryID::getValue))
        .toList();
  }

}
