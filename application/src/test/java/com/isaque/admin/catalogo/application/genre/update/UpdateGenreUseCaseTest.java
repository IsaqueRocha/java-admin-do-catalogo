package com.isaque.admin.catalogo.application.genre.update;

import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UpdateGenreUseCaseTest {
  @Mock
  private CategoryGateway categoryGateway;

  @Mock
  private GenreGateway genreGateway;

  @InjectMocks
  private DefaultUpdateGenreUseCase useCase;


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

  private List<String> asString(final List<CategoryID> categories) {
    return categories.stream().map(CategoryID::getValue).toList();
  }

}
