package com.isaque.admin.catalogo.application.category.update;

import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.exceptions.DomainException;
import com.isaque.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanup() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_thenShouldReturnCategoryId() {
        final var category = Category.newCategory(
                "Film",
                "A categoria",
                false
        );

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                category.getId().getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.with(category)));
        Mockito.when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .findById(Mockito.eq(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1))
                .update(Mockito.argThat(
                        updatedCategory ->
                                Objects.equals(expectedName, updatedCategory.getName())
                                        && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                        && Objects.equals(expectedIsActive, updatedCategory.isActive())
                                        && Objects.equals(expectedId, updatedCategory.getId())
                                        && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                        && updatedCategory.getUpdatedAt().isAfter(category.getUpdatedAt())
                                        && Objects.isNull(updatedCategory.getDeletedAt())
                ));
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var category = Category.newCategory("Film", null, true);
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assitida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' must not be null";
        final var expectedErrorCount = 1;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(Category.with(category)));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(0)).update(any());
    }

    @Test
    void givenAValidInactivateCommand_whenCallsUpdateCategory_thenShouldReturnInactiveCategoryId() {
        final var category = Category.newCategory(
                "Film",
                "A categoria",
                true
        );

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                category.getId().getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.with(category)));
        Mockito.when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var actualOutput = useCase.execute(command).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .findById(Mockito.eq(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1))
                .update(Mockito.argThat(
                        updatedCategory ->
                                Objects.equals(expectedName, updatedCategory.getName())
                                        && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                        && Objects.equals(expectedIsActive, updatedCategory.isActive())
                                        && Objects.equals(expectedId, updatedCategory.getId())
                                        && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                        && updatedCategory.getUpdatedAt().isAfter(category.getUpdatedAt())
                                        && Objects.nonNull(updatedCategory.getDeletedAt())
                ));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldReturnAnException() {
        // given
        final var category = Category.newCategory(
                "Film",
                "A categoria",
                true
        );
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.with(category)));
        Mockito.when(categoryGateway.update(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        // when
        final var notification = useCase.execute(command).getLeft();

        // then
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .update(Mockito.argThat(
                        updatedCategory ->
                                Objects.equals(expectedName, updatedCategory.getName())
                                        && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                        && Objects.equals(expectedIsActive, updatedCategory.isActive())
                                        && Objects.equals(expectedId, updatedCategory.getId())
                                        && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                        && updatedCategory.getUpdatedAt().isAfter(category.getUpdatedAt())
                                        && Objects.isNull(updatedCategory.getDeletedAt())
                ));
    }

    @Test
    void givenACommandWithInvalidId_whenCallsUpdateCategory_thenShouldReturnNotFoundException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assitida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category with id 123 was not found";
        final var expectedId = "123";

        final var command = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(CategoryID.from(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(0)).update(any());
    }

}
