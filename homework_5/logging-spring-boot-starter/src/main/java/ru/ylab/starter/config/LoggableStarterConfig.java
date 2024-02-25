package ru.ylab.starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import ru.ylab.starter.aop.aspects.AuditAspect;
import ru.ylab.starter.aop.aspects.LoggableAspect;
import ru.ylab.starter.service.AuditService;
import ru.ylab.starter.service.AuditServiceImpl;

/**
 * The type Starter config.
 */
@AutoConfiguration
public class LoggableStarterConfig {
    /**
     * Loggable aspect.
     *
     * @return the loggable aspect
     */
    @Bean
    @ConditionalOnMissingBean
    public LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }

    /**
     * Audit service audit service.
     *
     * @return the audit service
     */

    @Bean
    @ConditionalOnEnableAudit
    public AuditService auditService() {
        return new AuditServiceImpl();
    }

    /**
     * Audit aspect.
     *
     * @param auditService the audit service
     * @return the audit aspect
     */

    @Bean
    @ConditionalOnEnableAudit
    public AuditAspect auditAspect(AuditService auditService) {
        return new AuditAspect(auditService);
    }
}
