package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class ThreadLocalLogTrace implements LogTrace {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

//    private TraceId traceIdHolder; //traceId 동기화, 동시성 이슈 발생
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>(); //traceId 동기화, 동시성 이슈 발생

    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}]{}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(i == level - 1 ? "|" + prefix : "|  ");
        }
        return sb.toString();
    }

    private void complete(TraceStatus status, Exception e) {
        long time = System.currentTimeMillis() - status.getStartTimeMs();
        if (e == null) {
            log.info("[{}]{}{} time={}", status.getTraceId().getId(), addSpace(COMPLETE_PREFIX, status.getTraceId().getLevel()), status.getMessage(), time);
        } else {
            log.info("[{}]{}{} time={}ms ex={}", status.getTraceId().getId(), addSpace(EX_PREFIX, status.getTraceId().getLevel()), status.getMessage(), time, e.getMessage());
        }

        releaseTraceId();
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
            traceIdHolder.remove(); //사용후에 해당 쓰레드가 저장했던 데이터를 다 지워야 한다.
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }
}
