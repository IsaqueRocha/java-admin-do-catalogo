package com.isaque.admin.catalogo.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    @Test
    void testNewCategory() {
        Assertions.assertNotNull(new Category());
    }
}