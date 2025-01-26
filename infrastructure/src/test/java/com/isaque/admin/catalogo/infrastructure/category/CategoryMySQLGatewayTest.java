package com.isaque.admin.catalogo.infrastructure.category;

import com.isaque.admin.catalogo.infrastructure.MySQLGatewayTest;
import com.isaque.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
class CategoryMySQLGatewayTest {
    @Autowired
    private CategoryMySQLGateway gateway;
    @Autowired
    private CategoryRepository repository;

    @Test
    void testInjectedDependencies() {
        Assertions.assertNotNull(gateway);
        Assertions.assertNotNull(repository);
    }
}