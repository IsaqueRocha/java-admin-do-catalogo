package com.isaque.admin.catalogo.application.genre.update;

import com.isaque.admin.catalogo.application.UseCaseTest;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.exceptions.NotificationException;
import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.times;

class UpdateGenreUseCaseTest extends UseCaseTest {
  @Mock
  private CategoryGateway categoryGateway;

  @Mock
  private GenreGateway genreGateway;

  @InjectMocks
  private DefaultUpdateGenreUseCase useCase;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway, genreGateway);
  }

  @Test
  void givenAValidCommand_whenCallsUpdateGenre_thenShouldReturnGenreId() {
    // given
    final var genre = Genre.newGenre("acao", true);

    final var expectedId = genre.getId();
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var command = UpdateGenreCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive,
        asString(expectedCategories));

    Mockito.when(genreGateway.findById(Mockito.any()))
        .thenReturn(Optional.of(Genre.with(genre)));

    Mockito.when(genreGateway.update(Mockito.any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(command);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(genreGateway, times(1))
        .findById(expectedId);

    Mockito.verify(genreGateway, times(1)).update(
        Mockito.argThat(updatedGenre ->
            Objects.equals(expectedId, updatedGenre.getId()) &&
            Objects.equals(expectedIsActive, updatedGenre.isActive()) &&
            Objects.equals(expectedName, updatedGenre.getName()) &&
            Objects.equals(expectedCategories, updatedGenre.getCategories()) &&
            Objects.equals(genre.getCreatedAt(), updatedGenre.getCreatedAt()) &&
            Objects.isNull(updatedGenre.getDeletedAt()) &&
            genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
        ));
  }

  @Test
  void givenAValidCommandWithCategories_whenCallsUpdateGenre_thenShouldReturnGenreId() {
    // given
    final var genre = Genre.newGenre("acao", true);

    final var expectedId = genre.getId();
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(
        CategoryID.from("123"),
        CategoryID.from("456")
    );

    final var command = UpdateGenreCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive,
        asString(expectedCategories));

    Mockito.when(genreGateway.findById(Mockito.any()))
        .thenReturn(Optional.of(Genre.with(genre)));

    Mockito.when(categoryGateway.existsByIds(Mockito.any()))
        .thenReturn(expectedCategories);

    Mockito.when(genreGateway.update(Mockito.any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(command);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(genreGateway, times(1))
        .findById(expectedId);

    Mockito.verify(categoryGateway, times(1))
        .existsByIds(expectedCategories);

    Mockito.verify(genreGateway, times(1)).update(
        Mockito.argThat(updatedGenre ->
            Objects.equals(expectedId, updatedGenre.getId()) &&
            Objects.equals(expectedIsActive, updatedGenre.isActive()) &&
            Objects.equals(expectedName, updatedGenre.getName()) &&
            Objects.equals(expectedCategories, updatedGenre.getCategories()) &&
            Objects.equals(genre.getCreatedAt(), updatedGenre.getCreatedAt()) &&
            Objects.isNull(updatedGenre.getDeletedAt()) &&
            genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
        ));
  }

  @Test
  void givenAnInvalidName_whenCallsUpdateGenre_thenShouldReturnNotificationException() {
    // given
    final var genre = Genre.newGenre("acao", true);

    final var expectedId = genre.getId();
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' must not be null";

    final var command = UpdateGenreCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive,
        asString(expectedCategories));

    Mockito.when(genreGateway.findById(Mockito.any()))
        .thenReturn(Optional.of(Genre.with(genre)));


    // when
    final var actualException = Assertions.assertThrows(
        NotificationException.class, () -> useCase.execute(command)
    );

    // then
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());

    Mockito.verify(genreGateway, times(1))
        .findById(expectedId);

    Mockito.verify(categoryGateway, times(0))
        .existsByIds(expectedCategories);

    Mockito.verify(genreGateway, times(0))
        .update(Mockito.any());
  }

  @Test
  void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnNotificationException() {
    // given
    final var genre = Genre.newGenre("acao", true);

    final var series = CategoryID.from("123");
    final var filmes = CategoryID.from("456");
    final var documentarios = CategoryID.from("789");

    final var expectedId = genre.getId();
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes, series, documentarios);

    final var expectedErrorCount = 2;
    final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
    final var expectedErrorMessageTwo = "'name' must not be null";

    final var command = UpdateGenreCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive,
        asString(expectedCategories));

    Mockito.when(genreGateway.findById(Mockito.any()))
        .thenReturn(Optional.of(Genre.with(genre)));

    Mockito.when(categoryGateway.existsByIds(Mockito.any()))
        .thenReturn(List.of(series));


    // when
    final var actualException = Assertions.assertThrows(
        NotificationException.class, () -> useCase.execute(command)
    );

    // then
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().getFirst().message());
    Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().getLast().message());

    Mockito.verify(genreGateway, times(1))
        .findById(expectedId);

    Mockito.verify(categoryGateway, times(1))
        .existsByIds(expectedCategories);

    Mockito.verify(genreGateway, times(0))
        .update(Mockito.any());
  }

  @Test
  void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_thenShouldReturnGenreId() {
    // given
    final var genre = Genre.newGenre("acao", true);

    final var expectedId = genre.getId();
    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var command = UpdateGenreCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive,
        asString(expectedCategories));

    Mockito.when(genreGateway.findById(Mockito.any()))
        .thenReturn(Optional.of(Genre.with(genre)));

    Mockito.when(genreGateway.update(Mockito.any()))
        .thenAnswer(returnsFirstArg());

    Assertions.assertTrue(genre.isActive());
    Assertions.assertNull(genre.getDeletedAt());

    // when
    final var actualOutput = useCase.execute(command);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(genreGateway, times(1))
        .findById(expectedId);

    Mockito.verify(genreGateway, times(1)).update(
        Mockito.argThat(updatedGenre ->
            Objects.equals(expectedId, updatedGenre.getId()) &&
            Objects.equals(expectedIsActive, updatedGenre.isActive()) &&
            Objects.equals(expectedName, updatedGenre.getName()) &&
            Objects.equals(expectedCategories, updatedGenre.getCategories()) &&
            Objects.equals(genre.getCreatedAt(), updatedGenre.getCreatedAt()) &&
            Objects.nonNull(updatedGenre.getDeletedAt()) &&
            genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
        ));
  }

  private List<String> asString(final List<CategoryID> categories) {
    return categories.stream().map(CategoryID::getValue).toList();
  }
}
