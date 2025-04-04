package com.isaque.admin.catalogo.infrastructure.genre;

import com.isaque.admin.catalogo.MySQLGatewayTest;
import com.isaque.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.isaque.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
class GenreMySQLGatewayTest {
  @Autowired
  private CategoryMySQLGateway categoryMySQLGateway;

  @Autowired
  private GenreMySQLGateway genreMySQLGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  void testDependencyInjection() {
    Assertions.assertNotNull(categoryMySQLGateway);
    Assertions.assertNotNull(genreMySQLGateway);
    Assertions.assertNotNull(genreRepository);
  }
}
