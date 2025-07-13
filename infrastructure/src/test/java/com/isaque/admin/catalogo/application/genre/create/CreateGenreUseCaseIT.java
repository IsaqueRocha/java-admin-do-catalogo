package com.isaque.admin.catalogo.application.genre.create;

import com.isaque.admin.catalogo.IntegrationTest;
import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.exceptions.NotificationException;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

@IntegrationTest
public class CreateGenreUseCaseIT {
  @Autowired
  private DefaultCreateGenreUseCase useCase;

  @MockitoSpyBean
  private CategoryGateway categoryGateway;

  @MockitoSpyBean
  private GenreGateway genreGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  void givenAValidCommand_whenCallsCreateGenre_thenShouldReturnGenreId() {
    // given
    final var filmes =
        categoryGateway.create(Category.newCategory("Filmes", null, true));
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId());

    final var command =
        CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualOutput = useCase.execute(command);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    final var actualGenre = genreRepository.findById(actualOutput.id()).get();
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertTrue(
        expectedCategories.size() == actualGenre.getCategoryIds().size() &&
        expectedCategories.containsAll(actualGenre.getCategoryIds())
    );
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAAValidCommandWithoutCategories_whenCallsCreateGenre_thenShouldReturnGenreId() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of(
    );

    final var command =
        CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualOutput = useCase.execute(command);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    final var actualGenre = genreRepository.findById(actualOutput.id()).get();
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertTrue(
        expectedCategories.size() == actualGenre.getCategoryIds().size() &&
        expectedCategories.containsAll(actualGenre.getCategoryIds())
    );
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_thenShouldReturnGenreId() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualOutput = useCase.execute(command);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    final var actualGenre = genreRepository.findById(actualOutput.id()).get();
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertTrue(
        expectedCategories.size() == actualGenre.getCategoryIds().size() &&
        expectedCategories.containsAll(actualGenre.getCategoryIds())
    );
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNotNull(actualGenre.getDeletedAt());
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
    final var series = categoryGateway.create(Category.newCategory("Séries", "Uma categoria assistida", true));
    final var filmes = CategoryID.from("456");
    final var documentarios = CategoryID.from("789");

    final var expectedName = " ";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes, series.getId(), documentarios);

    final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
    final var expectedErrorMessageTwo = "'name' must not be empty";
    final var expectedErrorCount = 2;

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
