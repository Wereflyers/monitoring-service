package ru.ylab.starter.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * The annotation Conditional on enable audit.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({AuditStarterConfig.class})
@Documented
public @interface EnableAuditStarter {
}