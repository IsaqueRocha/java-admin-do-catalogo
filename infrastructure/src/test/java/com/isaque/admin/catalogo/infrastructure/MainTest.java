package com.isaque.admin.catalogo.infrastructure;

import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.isaque.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

class MainTest {
    @Test
    void testMain() {
        Assertions.assertNotNull(new Main());
        Main.main(new String[]{});
    }
}

