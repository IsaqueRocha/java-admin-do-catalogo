package com.isaque.admin.catalogo.application.genre.retrieve.list;

import com.isaque.admin.catalogo.application.UseCaseTest;
import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.domain.pagination.Pagination;
import com.isaque.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListGenreUseCaseTest extends UseCaseTest {
  @InjectMocks
  private DefaultListGenreUseCase useCase;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(genreGateway);
  }

  @Test
  void givenAValidQuery_whenCallsListGenre_thenShouldReturnGenres() {
    // given
    final var genres = List.of(
        Genre.newGenre("Ação", true),
        Genre.newGenre("Aventura", true)
    );

    final var expectedItems = genres.stream().map(GenreListOutput::from).toList();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "A";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var expectedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        expectedTotal,
        genres
    );

    Mockito.when(genreGateway.findAll(Mockito.any()))
        .thenReturn(expectedPagination);

    final var searchQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualOutput = useCase.execute(searchQuery);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

    Mockito.verify(genreGateway, Mockito.times(1)).findAll(searchQuery);
  }

  @Test
  void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_thenShouldReturnGenres() {
    // given
    final var genres = List.<Genre>of();

    final var expectedItems = List.<GenreListOutput>of();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "A";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var expectedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        expectedTotal,
        genres
    );

    Mockito.when(genreGateway.findAll(Mockito.any()))
        .thenReturn(expectedPagination);

    final var searchQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualOutput = useCase.execute(searchQuery);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedPage, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

    Mockito.verify(genreGateway, Mockito.times(1)).findAll(searchQuery);
  }

  @Test
  void givenAValidQuery_whenCallsListGenreAndGatewayThrowsRandomError_thenShouldReturnException() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "A";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var expectedErrorMessage = "Gateway error";

    Mockito.when(genreGateway.findAll(Mockito.any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var searchQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualOutput = Assertions.assertThrows(
        IllegalStateException.class,
        () -> useCase.execute(searchQuery)
    );

    // then
    Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

    Mockito.verify(genreGateway, Mockito.times(1)).findAll(searchQuery);
  }
}
