package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;

@Slf4j
@Aspect
public class AspectV6Advice {

// Advice 중 @Around 만 ProceedingJoinPoint 를 인자로 받는다.
// ProceedingJoinPoint 안에는 proceed() 함수가 포함된다.
// proceed()를 호출하지 않으면 target 이 실행되지 않는다.
// @Around 외에 다른 Advice 들은 작업 흐름을 변경할 수 없어서 안전하다.
// @Around 외에 다른 Advice 들은 의도가 명확하다.
// 좋은 설계는 제약이 있는 것이다.

//    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            //@Before
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            //@AfterReturning
            log.info("[트랜잭션 성공] {}", joinPoint.getSignature());
            return result;
        } catch (Exception ex) {
            //@AfterThrowing
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw ex;
        } finally {
            //@After
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) { //  returning 속성에 사용한 이름과 메서드 매개변수명이 일치해야 한다.
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
        // result 를 return 하지는 않기 때문에 결과를 바꿀 수는 없음
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.allOrder()", returning = "result")
    public void doReturn2(JoinPoint joinPoint, String result) { // return 값의 타입이 맞지 않으면 advice 가 실행되지 않는다.
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.allOrder()", returning = "result")
    public void doReturn3(JoinPoint joinPoint, Integer result) { // return 값의 타입이 맞지 않으면 advice 가 실행되지 않는다.
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) { // throwing 속성에 사용한 이름과 메서드 매개변수명이 일치해야 한다.
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }

    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
        // 일반적으로 리소스를 해제하는 데 사용한다.
    }
}
