package hello.advanced.app.v3;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {
    private final LogTrace trace;

    public void save(TraceId traceId, String itemId) {
        TraceStatus status = trace.begin("OrderController.save()");
        try {
            if(itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            trace.end(status);

        } catch (Exception e) {
            trace.exception(status, e);
            throw e; // 예외를 먹지말고 꼭 다시 던지자
        }
        sleep(1000);
    }

    private void sleep(int millis) {
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
