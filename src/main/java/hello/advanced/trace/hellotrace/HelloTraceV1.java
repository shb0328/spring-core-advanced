package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTraceV1 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "|<X-";


    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}]{}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    public void end(TraceStatus status) {
        complete(status, null);
    }

    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(i ==level-1 ? "|" + prefix : "|  ");
        }
        return sb.toString();
    }

    private void complete(TraceStatus status, Exception e) {
        long time = System.currentTimeMillis() - status.getStartTimeMs();
        if (e == null) {
            log.info("[{}] {} time={}", status.getTraceId().getId(), addSpace(COMPLETE_PREFIX, status.getTraceId().getLevel()), time);
        } else {
            log.info("[{}] {} time={}ms ex={}", status.getTraceId().getId(), addSpace(EX_PREFIX, status.getTraceId().getLevel()), time,e.getMessage());
        }
    }
}
