package com.isaque.admin.catalogo.application;

import com.isaque.admin.catalogo.domain.category.Category;

public class UseCase {
    public Category execute() {
        return Category.newCategory("Filmes", "A categoria mais assistida", true);
    }
}