package com.isaque.admin.catalogo.e2e.category;

import com.isaque.admin.catalogo.E2ETest;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.isaque.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.isaque.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.isaque.admin.catalogo.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

@E2ETest
@Testcontainers
public class CategoryE2ETest {
    @SuppressWarnings("resource")
    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER =
            new MySQLContainer<>("mysql:latest")
                    .withPassword("123456")
                    .withUsername("root")
                    .withDatabaseName("adm_videos");
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CategoryRepository categoryRepository;

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var actualId = givenACategory(
                expectedName,
                expectedDescription,
                expectedIsActive);
        final var actualCategory = retrieveACategory(actualId.getValue());

        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNotNull(actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());
    }


    private CategoryID givenACategory(
            final String expectedName,
            final String expectedDescription,
            final boolean expectedIsActive
    ) throws Exception {
        final var requestBody = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(requestBody));

        final var actualId = Objects.requireNonNull(this.mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader("Location"))
                .replace("/categories/", "");

        return CategoryID.from(actualId);
    }

    private CategoryResponse retrieveACategory(final String id) throws Exception {
        final var request = MockMvcRequestBuilders.get("/categories/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Json.readValue(json, CategoryResponse.class);
    }
}
