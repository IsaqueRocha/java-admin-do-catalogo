package com.isaque.admin.catalogo.application.genre.create;

import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;


@ExtendWith(MockitoExtension.class)
class CreateGenreUseCaseTest {
    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;


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

        Mockito.verify(genreGateway, Mockito.atMostOnce())
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

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }
}