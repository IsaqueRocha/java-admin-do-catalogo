package com.isaque.admin.catalogo.application;

import com.isaque.admin.catalogo.domain.category.Category;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(final IN input);
}