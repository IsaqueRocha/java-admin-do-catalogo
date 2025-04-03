package com.isaque.admin.catalogo.application.genre.create;

import com.isaque.admin.catalogo.application.UseCaseTest;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.exceptions.NotificationException;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

class CreateGenreUseCaseTest extends UseCaseTest {
  @InjectMocks
  private DefaultCreateGenreUseCase useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway, genreGateway);
  }

  @Test
  void givenAValidCommand_whenCallsCreateGenre_thenShouldReturnGenreId() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    Mockito.when(genreGateway.create(Mockito.any()))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(command);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(genreGateway, Mockito.times(1))
        .create(Mockito.argThat(genre ->
            Objects.equals(expectedName, genre.getName()) &&
            Objects.equals(expectedIsActive, genre.isActive()) &&
            Objects.equals(expectedCategories, genre.getCategories()) &&
            Objects.nonNull(genre.getId()) &&
            Objects.nonNull(genre.getCreatedAt()) &&
            Objects.nonNull(genre.getUpdatedAt()) &&
            Objects.isNull(genre.getDeletedAt())
        ));
  }

  @Test
  void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_thenShouldReturnGenreId() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    Mockito.when(genreGateway.create(Mockito.any()))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(command);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(genreGateway, Mockito.times(1))
        .create(Mockito.argThat(genre ->
            Objects.equals(expectedName, genre.getName()) &&
            Objects.equals(expectedIsActive, genre.isActive()) &&
            Objects.equals(expectedCategories, genre.getCategories()) &&
            Objects.nonNull(genre.getId()) &&
            Objects.nonNull(genre.getCreatedAt()) &&
            Objects.nonNull(genre.getUpdatedAt()) &&
            Objects.nonNull(genre.getDeletedAt())
        ));

  }

  @Test
  void givenAAValidCommandWithCategories_whenCallsCreateGenre_thenShouldReturnGenreId() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(
        CategoryID.from("123"),
        CategoryID.from("456")
    );

    final var command =
        CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    Mockito.when(categoryGateway.existsByIds(Mockito.any()))
        .thenReturn(expectedCategories);

    Mockito.when(genreGateway.create(Mockito.any()))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(command);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(categoryGateway, Mockito.times(1))
        .existsByIds(expectedCategories);

    Mockito.verify(genreGateway, Mockito.times(1))
        .create(Mockito.argThat(genre ->
            Objects.equals(expectedName, genre.getName()) &&
            Objects.equals(expectedIsActive, genre.isActive()) &&
            Objects.equals(expectedCategories, genre.getCategories()) &&
            Objects.nonNull(genre.getId()) &&
            Objects.nonNull(genre.getCreatedAt()) &&
            Objects.nonNull(genre.getUpdatedAt()) &&
            Objects.isNull(genre.getDeletedAt())
        ));
  }

  @Test
  void givenAInvalidEmptyName_whenCallsCrateGenre_thenShouldReturnDomainException() {
    // given
    final var expectedName = " ";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var expectedErrorMessage = "'name' must not be empty";
    final var expectedErrorCount = 1;

    final var command =
        CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(command);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());

    Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
    Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
  }

  @Test
  void givenAInvalidNullName_whenCallsCrateGenre_thenShouldReturnDomainException() {
    // given
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var expectedErrorMessage = "'name' must not be null";
    final var expectedErrorCount = 1;

    final var command =
        CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(command);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());

    Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
    Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
  }

  @Test
  void givenAValidCommand_whenCallsCrateGenreAndSomeCategoriesDoesNotExist_thenShouldReturnDomainException() {
    // given
    final var series = CategoryID.from("123");
    final var filmes = CategoryID.from("456");
    final var documentarios = CategoryID.from("789");


    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes, series, documentarios);

    final var expectedErrorMessage = "Some categories could not be found: 456, 789";
    final var expectedErrorCount = 1;

    Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(series));

    final var command =
        CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(command);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());

    Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
    Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
  }

  @Test
  void givenANInValidName_whenCallsCrateGenreAndSomeCategoriesDoesNotExist_thenShouldReturnDomainException() {
    // given
    final var series = CategoryID.from("123");
    final var filmes = CategoryID.from("456");
    final var documentarios = CategoryID.from("789");


    final var expectedName = " ";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes, series, documentarios);

    final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
    final var expectedErrorMessageTwo = "'name' must not be empty";
    final var expectedErrorCount = 2;

    Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(series));

    final var command =
        CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(command);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().getFirst().message());
    Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

    Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
    Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
  }

  private List<String> asString(final List<CategoryID> categories) {
    return categories.stream().map(CategoryID::getValue).toList();
  }
}