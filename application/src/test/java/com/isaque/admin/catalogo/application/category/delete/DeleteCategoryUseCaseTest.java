package com.isaque.admin.catalogo.application.category.delete;

import com.isaque.admin.catalogo.application.UseCaseTest;
import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteCategoryUseCaseTest extends UseCaseTest {
  @InjectMocks
  private DefaultDeleteCategoryUseCase useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway);
  }

  @Test
  void givenAValidId_whenCallsDeleteCategory_thenShouldBeOk() {
    final var category = Category.newCategory(
        "Filmes",
        "A categoria mais assistida",
        true);
    final var expectedId = category.getId();

    Mockito.doNothing().when(categoryGateway).deleteById(Mockito.eq(expectedId));

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(expectedId);
  }

  @Test
  void givenAnInvalidId_whenCallsDeleteCategory_thenShouldBeOk() {
    final var expectedId = CategoryID.from("123");

    Mockito.doNothing().when(categoryGateway).deleteById(Mockito.eq(expectedId));

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(expectedId);
  }

  @Test
  void givenAValidId_whenGatewayThrowsException_thenShouldBeOk() {
    final var category = Category.newCategory(
        "Filmes",
        "A categoria mais assistida",
        true);
    final var expectedId = category.getId();

    Mockito.doThrow(new IllegalStateException("Gateway error")).when(categoryGateway)
        .deleteById(Mockito.eq(expectedId));

    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(expectedId);
  }
}
