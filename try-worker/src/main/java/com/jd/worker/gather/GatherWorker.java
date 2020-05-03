package com.jd.worker.gather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class GatherWorker {
    @Autowired
    private GatherContext gatherContext;

    private ThreadPoolExecutor executor;
    private BlockingDeque<Runnable> workerQueue;
    private AtomicBoolean running = new AtomicBoolean(false);

    public GatherWorker(int coreSize, int maxPoolSize, long keepAlive, int queueSize){
        workerQueue = new LinkedBlockingDeque<>(queueSize);
        running.set(false);
        executor = new ThreadPoolExecutor(coreSize, maxPoolSize, keepAlive, TimeUnit.MILLISECONDS, workerQueue,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public boolean startGather(String dbName, String tbName, Date from, Date to){
        if (isRunning()) {
            log.info("当前有未完成任务!");
            return false;
        }

        if(dbName == null || "".equals(dbName.trim())){
            log.info("未选择db!");
            return false;
        }

        log.info(" Begin IndexAll worker, time: " + (new Date()).toString());
        running.set(true);

        for (int i=0; i<10; i++) {
            if (!running.get()) {
                break;
            }
            GatherTask task = new GatherTask(dbName, tbName+i, from, to, gatherContext);

            executor.execute(task);
            gatherContext.getTaskCount().incrementAndGet();
        }
        return true;
    }

    public synchronized boolean close(){
        if (!isRunning()) {
            return false;
        }
        gatherContext.setStop(true);
        running.set(false);
        return true;
    }

    public boolean isRunning() {
        return gatherContext.getTaskCount().get() > 0;
    }

    public int getWorkCount(){
        return gatherContext.getTaskCount().get();
    }

    public boolean isBusy(){
        return gatherContext.getTaskCount().get() > 500;
    }
}