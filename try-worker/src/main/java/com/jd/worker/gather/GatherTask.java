package com.jd.worker.gather;

import com.jd.service.WorkerService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Data
public class GatherTask implements Runnable{
    private String dbName;
    private String tbName;
    private Date from;
    private Date to;
    private GatherContext context;

    public GatherTask (String dbName, String tbName, Date from, Date to, GatherContext gatherContext){
        this.dbName = dbName;
        this.tbName = tbName;
        this.from = from;
        this.to = to;
        this.context = gatherContext;
    }

    @Override
    public void run() {
        WorkerService service = context.getWorkerService();
        try {
            if (!context.isStop()) {
                service.gatherWork(dbName, tbName, from, to);
            }
        } catch (Exception e) {
            log.error("GatherTask error happens!", e);
        } finally {
            if (context.getTaskCount().decrementAndGet() == 0) {
                log.info("All task are completed! End time is " + (new Date().toString()));
            }
        }
    }
}