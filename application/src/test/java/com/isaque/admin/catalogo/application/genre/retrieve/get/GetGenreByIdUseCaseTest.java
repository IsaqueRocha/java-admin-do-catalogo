package com.isaque.admin.catalogo.application.genre.retrieve.get;

import com.isaque.admin.catalogo.application.UseCaseTest;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.exceptions.NotFoundException;
import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetGenreByIdUseCaseTest extends UseCaseTest {
  @InjectMocks
  private DefaultGetGenreByIdUseCase useCase;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(genreGateway);
  }

  @Test
  void givenAValidId_whenCallsGetGenreById_thenShouldReturnGenre() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(
        CategoryID.from("123"),
        CategoryID.from("456")
    );

    final var genre = Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories);

    final var expectedId = genre.getId();

    Mockito.when(genreGateway.findById(Mockito.any()))
        .thenReturn(Optional.of(Genre.with(genre)));

    // when
    final var actualGenre = useCase.execute(genre.getId().getValue());

    // then
    Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
    Assertions.assertEquals(expectedName, actualGenre.name());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
    Assertions.assertEquals(genre.getCreatedAt(), actualGenre.createdAt());
    Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.updatedAt());
    Assertions.assertEquals(genre.getDeletedAt(), actualGenre.deletedAt());

    Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
  }

  @Test
  void givenAValidId_whenCallsGetGenreAndDoesNotExists_thenShouldReturnNotFound() {
    // given
    final var expectedErrorMessage = "Genre with id 123 was not found";

    final var expectedId = GenreID.from("123");

    Mockito.when(genreGateway.findById(Mockito.any()))
        .thenReturn(Optional.empty());

    // when
    final var actualException = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(expectedId.getValue())
    );

    // then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());


    Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
  }

  private List<String> asString(final List<CategoryID> categories) {
    return categories.stream()
        .map(CategoryID::getValue)
        .toList();
  }
}
