package com.jd.service.worker;

import java.util.Date;

public interface WorkerService {
    void gatherWork(String dbName, String tbName, Date from, Date to);
}