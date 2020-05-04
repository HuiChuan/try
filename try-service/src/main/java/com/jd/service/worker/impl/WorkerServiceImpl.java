package com.jd.service.worker.impl;

import com.jd.service.worker.WorkerService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class WorkerServiceImpl implements WorkerService{
    public void gatherWork(String dbName, String tbName, Date from, Date to){
        try {
            log.info("WorkerServiceImpl runs [" + dbName + "," + tbName + "]");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("WorkerServiceImpl gatherWork error happens!", e);
        }
    }
}