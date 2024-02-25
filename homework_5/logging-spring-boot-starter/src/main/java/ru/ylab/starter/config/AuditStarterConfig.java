package ru.ylab.starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import ru.ylab.starter.aop.aspects.AuditAspect;
import ru.ylab.starter.service.AuditService;
import ru.ylab.starter.service.AuditServiceImpl;

/**
 * The type Audit starter config.
 */
@AutoConfiguration
public class AuditStarterConfig {

    /**
     * Audit service audit service.
     *
     * @return the audit service
     */
    @Bean
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
