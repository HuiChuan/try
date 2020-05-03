package com.jd.worker.gather;

import com.jd.service.WorkerService;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class GatherContext {
    private int pageSize;
    private WorkerService workerService;
    private final AtomicInteger taskCount = new AtomicInteger(0);
    private boolean stop;
}