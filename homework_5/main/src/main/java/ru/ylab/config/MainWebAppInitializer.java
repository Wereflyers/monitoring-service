package ru.ylab.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * The type Main web app initializer.
 */
@SpringBootApplication
@ComponentScan(basePackages = "ru.ylab.*")
//@EnableAuditStarter
public class MainWebAppInitializer extends SpringBootServletInitializer {
}