package com.isaque.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaque.admin.catalogo.ControllerTest;
import com.isaque.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.isaque.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.isaque.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.isaque.admin.catalogo.application.category.retrive.get.CategoryOutput;
import com.isaque.admin.catalogo.application.category.retrive.get.GetCategoryByIdUseCase;
import com.isaque.admin.catalogo.application.category.retrive.list.CategoryListOutput;
import com.isaque.admin.catalogo.application.category.retrive.list.ListCategoriesUseCase;
import com.isaque.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.isaque.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.exceptions.DomainException;
import com.isaque.admin.catalogo.domain.exceptions.NotFoundException;
import com.isaque.admin.catalogo.domain.pagination.Pagination;
import com.isaque.admin.catalogo.domain.validation.Error;
import com.isaque.admin.catalogo.domain.validation.handler.Notification;
import com.isaque.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.isaque.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockitoBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockitoBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockitoBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var input = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from(("123"))));

        //when

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categories/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("123")
                );

        Mockito.verify(createCategoryUseCase, Mockito.times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));

    }

    @Test
    void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' must not be null";
        final var input = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        //when

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(input));

        // then
        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value(expectedErrorMessage));

        Mockito.verify(createCategoryUseCase, Mockito.times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' must not be null";
        final var input = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        //when

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value(expectedErrorMessage));

        Mockito.verify(createCategoryUseCase, Mockito.times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    void givenAValidCategoryId_whenCallsGetCategoryById_thenShouldReturnCategory() throws Exception {
        // given
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var expectedId = category.getId().getValue();

        // when
        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenReturn(CategoryOutput.from(category));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(category.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(category.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(category.getDeletedAt())));

        Mockito.verify(getCategoryByIdUseCase, Mockito.times(1))
                .execute(expectedId);
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "Category with id 123 was not found";
        final var expectedId = CategoryID.from("123");

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        // when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_thenShouldReturnCategoryId() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var command = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

        //when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    void givenACommandWithAInvalidID_whenCallsUpdateCategory_thenShouldReturnNotFoundException() throws Exception {
        // given
        final var expectedId = "not-found";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with id " + expectedId + " was not found";

        final var command = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        //when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string(
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(jsonPath(
                        "$.message", equalTo(expectedErrorMessage)
                ));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final var expectedId = "not-found";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must not be null";

        final var command = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        //when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(jsonPath(
                        "$.errors", hasSize(expectedErrorCount)
                ))
                .andExpect(jsonPath(
                        "$.errors[0].message", equalTo(expectedErrorMessage)
                ));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_thenShouldReturnNoContent() throws Exception {
        // given
        final var expectedId = "123";

        Mockito.doNothing().when(deleteCategoryUseCase).execute(any());

        // when
        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isNoContent());

        Mockito.verify(deleteCategoryUseCase, Mockito.atMostOnce()).execute(expectedId);
    }

    @Test
    void givenValidParams_whenCallsListCategories_thenShouldReturnCategories() throws Exception {
        // given
        final var category = Category.newCategory(
                "Movies",
                null,
                true
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CategoryListOutput.from(category));

        Mockito.when(listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<>(
                        expectedPage,
                        expectedPerPage,
                        expectedTotal,
                        expectedItems
                ));

        final var params = Map.of(
                "page", String.valueOf(expectedPage),
                "perPage", String.valueOf(expectedPerPage),
                "sort", expectedSort,
                "dir", expectedDirection,
                "search", expectedTerms
        );

        //when
        final var request = MockMvcRequestBuilders.get("/categories")
                .queryParams(MultiValueMap.fromSingleValue(params))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(category.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(category.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(category.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(category.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(category.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(category.getDeletedAt())));

        Mockito.verify(listCategoriesUseCase, Mockito.times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
