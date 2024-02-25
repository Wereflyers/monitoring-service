package ru.ylab.starter.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * The type Conditional on enable audit condition.
 */
public class OnEnableAuditCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !context.getBeanFactory().getBeansWithAnnotation(EnableAuditStarter.class).isEmpty();
    }
}