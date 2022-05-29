package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect // 컴포넌트 스캔의 대상 X, 별도의 빈등록 필요
public class AspectV4Pointcut {

    @Around("hello.aop.order.aop.Pointcuts.allOrder()") //패키지명.클래스명.Pointcut이름
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("[트랜잭션 성공] {}", joinPoint.getSignature());
            return result;
        } catch (Exception ex) {
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw ex;
        } finally {
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }
}
