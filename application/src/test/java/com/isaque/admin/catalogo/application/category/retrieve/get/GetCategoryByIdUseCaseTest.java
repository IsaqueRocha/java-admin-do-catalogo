package com.isaque.admin.catalogo.application.category.retrieve.get;

import com.isaque.admin.catalogo.application.category.retrive.get.DefaultGetCategoryByIdUseCase;
import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GetCategoryByIdUseCaseTest {
    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(
                "Filmes",
                "A categoria mais assistida",
                true);

        final var expectedId = category.getId();

        Mockito.when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));

        final var actualCategory = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.createdAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(category.getDeletedAt(), actualCategory.deletedAt());
    }

    @Test
    void givenAInvalidId_whenCallsGetCategory_shouldReturnsNotFound() {
        final var expectedErrorMessage = "Category with id 123 was not found";

        final var expectedId = CategoryID.from("123");

        Mockito.when(categoryGateway.findById(expectedId)).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_shouldReturnAnException() {
        final var expectedErrorMessage = "Gateway Error";

        final var expectedId = CategoryID.from("123");

        Mockito.when(categoryGateway.findById(expectedId)).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
