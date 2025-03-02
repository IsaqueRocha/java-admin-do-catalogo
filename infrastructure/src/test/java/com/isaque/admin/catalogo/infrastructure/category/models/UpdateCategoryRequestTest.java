package com.isaque.admin.catalogo.infrastructure.category.models;

import com.isaque.admin.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
class UpdateCategoryRequestTest {
    @Autowired
    private JacksonTester<CategoryResponse> json;

    @Test
    void testUnmarshall() throws IOException {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var jsonString = """
                {
                    "name": "%s",
                    "description": "%s",
                    "is_active": "%s"
                }
                """.formatted(
                expectedName,
                expectedDescription,
                expectedIsActive);

        final var actualJson = this.json.parse(jsonString);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
