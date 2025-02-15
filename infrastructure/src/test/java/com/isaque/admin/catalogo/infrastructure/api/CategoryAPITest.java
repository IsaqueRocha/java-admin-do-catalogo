package com.isaque.admin.catalogo.infrastructure.api;

import com.isaque.admin.catalogo.ControllerTest;
import com.isaque.admin.catalogo.application.category.create.CreateCategoryUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @Test
    void test() {
        Assertions.assertTrue(true);
    }
}
