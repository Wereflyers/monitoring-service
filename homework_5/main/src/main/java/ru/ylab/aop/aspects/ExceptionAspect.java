package ru.ylab.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * The type Loggable aspect.
 */
@Aspect
@Slf4j
@Component
public class ExceptionAspect {
    /**
     * Do recovery actions.
     *
     * @param joinPoint the join point
     * @param ex        the ex
     */
    @AfterThrowing(pointcut = "execution(* ru.ylab.in.*.*(..)) || execution(* ru.ylab.service.*.*(..))",
            throwing = "ex")
    public void doRecoveryActions(JoinPoint joinPoint, Throwable ex) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        String stuff = signature.toString();
        String arguments = Arrays.toString(joinPoint.getArgs());
        log.info("Write something in the log... We have caught exception in method: "
                + methodName + " with arguments "
                + arguments + "\nand the full toString: " + stuff + "\nthe exception is: "
                + ex.getMessage());
    }
}
