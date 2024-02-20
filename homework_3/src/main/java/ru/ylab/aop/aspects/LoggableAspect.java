package ru.ylab.aop.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

/**
 * The type Loggable aspect.
 */
@Aspect
public class LoggableAspect {
    /**
     * Annotated by loggable.
     */
    @Pointcut("within(@ru.ylab.aop.annotations.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() {

    }

    /**
     * Logging object.
     *
     * @param proceedingJoinPoint the proceeding join point
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Calling method " + proceedingJoinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }

    /**
     * Do recovery actions.
     *
     * @param joinPoint the join point
     * @param ex        the ex
     */
    @AfterThrowing(pointcut = "execution(* ru.ylab.in.servlets.*.*(..)) || execution(* ru.ylab.service.*.*(..))",
            throwing = "ex")
    public void doRecoveryActions(JoinPoint joinPoint, Throwable ex) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        String stuff = signature.toString();
        String arguments = Arrays.toString(joinPoint.getArgs());
        System.out.println("Write something in the log... We have caught exception in method: "
                + methodName + " with arguments "
                + arguments + "\nand the full toString: " + stuff + "\nthe exception is: "
                + ex.getMessage());
    }
}
