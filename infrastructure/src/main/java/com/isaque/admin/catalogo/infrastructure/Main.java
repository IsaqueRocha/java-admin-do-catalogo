package com.isaque.admin.catalogo.infrastructure;

import com.isaque.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.isaque.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.isaque.admin.catalogo.application.category.retrive.get.GetCategoryByIdUseCase;
import com.isaque.admin.catalogo.application.category.retrive.list.ListCategoriesUseCase;
import com.isaque.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.isaque.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

    ApplicationRunner runner(
            @Autowired CreateCategoryUseCase createCategoryUseCase,
            @Autowired DeleteCategoryUseCase deleteCategoryUseCase,
            @Autowired GetCategoryByIdUseCase getCategoryByIdUseCase,
            @Autowired ListCategoriesUseCase listCategoriesUseCase,
            @Autowired UpdateCategoryUseCase updateCategoryUseCase
            ) {
        return args -> {

        };
    }
}