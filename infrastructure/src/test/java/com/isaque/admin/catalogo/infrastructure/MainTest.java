package com.isaque.admin.catalogo.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void testMain() {
        Assertions.assertNotNull(new Main());
        Main.main(new String[]{});
    }
}