package com.isaque.admin.catalogo.application.genre.create;

import com.isaque.admin.catalogo.IntegrationTest;
import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

  private List<String> asString(final List<CategoryID> categories) {
    return categories.stream().map(CategoryID::getValue).toList();
  }
}
