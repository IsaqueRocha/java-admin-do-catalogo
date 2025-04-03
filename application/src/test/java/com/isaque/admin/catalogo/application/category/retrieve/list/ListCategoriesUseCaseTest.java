package com.isaque.admin.catalogo.application.category.retrieve.list;

import com.isaque.admin.catalogo.application.UseCaseTest;
import com.isaque.admin.catalogo.application.category.retrive.list.CategoryListOutput;
import com.isaque.admin.catalogo.application.category.retrive.list.DefaultListCategoriesUseCase;
import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.pagination.Pagination;
import com.isaque.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListCategoriesUseCaseTest extends UseCaseTest {
  @InjectMocks
  private DefaultListCategoriesUseCase useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway);
  }

  @Test
  void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
    final var categories = List.of(
        Category.newCategory("Filmes", null, true),
        Category.newCategory("Series", null, true)
    );

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var query = new SearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection
    );

    final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

    final var expectedItemsCount = 2;
    final var expectedResult = expectedPagination.map(CategoryListOutput::from);

    Mockito.when(categoryGateway.findAll(Mockito.eq(query))).thenReturn(expectedPagination);

    final var actualResult = useCase.execute(query);

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedResult, actualResult);
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(categories.size(), actualResult.total());

  }

  @Test
  void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
    final var categories = List.<Category>of();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var query = new SearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection
    );

    final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

    final var expectedItemsCount = 0;
    final var expectedResult = expectedPagination.map(CategoryListOutput::from);

    Mockito.when(categoryGateway.findAll(Mockito.eq(query))).thenReturn(expectedPagination);

    final var actualResult = useCase.execute(query);

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedResult, actualResult);
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(categories.size(), actualResult.total());
  }

  @Test
  void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnException() {
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var query = new SearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection
    );

    final var expectedErrorMessage = "Gateway error";

    Mockito.when(categoryGateway.findAll(Mockito.eq(query))).thenThrow(new IllegalStateException(expectedErrorMessage));

    final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }
}
