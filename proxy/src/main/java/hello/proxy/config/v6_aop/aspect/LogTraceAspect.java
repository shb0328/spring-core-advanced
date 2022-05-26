package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

@Slf4j
@Aspect
// AnnotationAwareAspectJAutoProxyCreator(자동 프록시 생성기) 는
// 1. @Aspect 를 찾아서 이것을 (내용을 보고) Advisor (1 PointCut + 1 Advise) 로 바꿔서 저장한다.
// 2. Advisor 를 자동으로 찾아와서 필요한 곳에 프록시를 생성하고 적용한다.
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    // advisor
    @Around("execution(* hello.proxy.app..*(..))") // pointCut
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { // advise
        TraceStatus status = null;
        try {
//            Object[] args = joinPoint.getArgs();
//            Object target = joinPoint.getTarget();
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            //로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}

