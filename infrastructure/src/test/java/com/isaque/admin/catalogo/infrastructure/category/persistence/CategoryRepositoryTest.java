package com.isaque.admin.catalogo.infrastructure.category.persistence;

import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.infrastructure.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository repository;

    @Test
    void givenAnInvalidNullName_whenCallsSave_thenShouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value: com.isaque.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity." + expectedPropertyName;
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setName(null);
        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());
        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullCreatedAt_whenCallsSave_thenShouldReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value: com.isaque.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity." + expectedPropertyName;
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setCreatedAt(null);
        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());
        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullUpdatedAt_whenCallsSave_thenShouldReturnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value: com.isaque.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity." + expectedPropertyName;
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setUpdatedAt(null);
        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());
        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }
}
