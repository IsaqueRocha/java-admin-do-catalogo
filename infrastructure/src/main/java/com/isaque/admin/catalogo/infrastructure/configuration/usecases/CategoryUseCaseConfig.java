package com.isaque.admin.catalogo.infrastructure.configuration.usecases;

import com.isaque.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.isaque.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.isaque.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.isaque.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.isaque.admin.catalogo.application.category.retrive.get.DefaultGetCategoryByIdUseCase;
import com.isaque.admin.catalogo.application.category.retrive.get.GetCategoryByIdUseCase;
import com.isaque.admin.catalogo.application.category.retrive.list.DefaultListCategoriesUseCase;
import com.isaque.admin.catalogo.application.category.retrive.list.ListCategoriesUseCase;
import com.isaque.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import com.isaque.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {
    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase  () {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoriesUseCase  () {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }
}
