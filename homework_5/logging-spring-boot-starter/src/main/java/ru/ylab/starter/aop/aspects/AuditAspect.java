package ru.ylab.starter.aop.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.ylab.starter.service.AuditService;

@Aspect
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditService auditService;

    @Pointcut("@within(ru.ylab.starter.aop.annotations.Auditable) && execution(public * *(..)) && " +
            "within(@org.springframework.web.bind.annotation.RestController *)")
    public void annotatedByAuditable() {
    }

    @Around(value = "annotatedByAuditable() && (args(username) || args(username,..) || args(..,username))")
    public Object audit(ProceedingJoinPoint proceedingJoinPoint, String username) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();
        ResponseEntity<?> result = (ResponseEntity<?>) proceedingJoinPoint.proceed();
        auditService.save(username + " " + methodName + " " + result.getBody());
        return result;
    }
}
