package com.isaque.admin.catalogo;

import com.isaque.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(MySQLCleanUpExtension.class)
public @interface IntegrationTest {
}
