package com.isaque.admin.catalogo.domain.genre;

import com.isaque.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GenreTest {
    @Test
    void givenAValidName_whenCallNewGenre_thenShouldInstantiateAGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;
        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must not be null";

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> {
                    Genre.newGenre(expectedName, expectedIsActive);
                });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidEmptyName_whenCallNewGenreAndValidate_thenShouldReceiveAError() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must not be empty";

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> {
                    Genre.newGenre(expectedName, expectedIsActive);
                });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_thenShouldReceiveAError() {
        final var expectedName = """
                Justo luptatum delenit magna, sea blandit nostrud aliquyam nisl enim suscipit voluptua 
                voluptate aliquip incidunt congue luptatum. Soluta ullamcorper augue gubergren zzril iure 
                accusam eu nonumy proident deserunt ad assum veniam tempor voluptua clita nostrud iure 
                ipsum rebum iure accumsan aute aliquip nisl duis zzril nonummy. Delenit sit laoreet dolor 
                ipsum enim invidunt voluptua exercitation facer accumsan ipsum mollit eu dignissim. Lobortis quod duo, 
                accusam diam amet tincidunt accumsan vel eu vulputate ullamco takimata adipisici illum commodi nihil 
                incidunt facilisis nibh non dolor. Feugiat iusto est in aliquyam tincidunt. Laborum voluptua enim magna.
                Justo tation culpa.
                """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> {
                    Genre.newGenre(expectedName, expectedIsActive);
                });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
