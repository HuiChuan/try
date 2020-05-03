package com.jd.web;

import com.jd.constants.Constants;
import com.jd.enums.GatherTypeEnum;
import com.jd.worker.gather.GatherWorker;
import com.jd.worker.index.IndexWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/worker")
public class WorkerController implements InitializingBean {

    @Autowired
    GatherWorker gatherWorker;

    @Autowired
    IndexWorker indexWorker;

    private Map<GatherTypeEnum, Map<String, Integer>> dbNameTbNameMap = new HashMap();

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView mav=new ModelAndView("worker");
        mav.addObject("gatherList", GatherTypeEnum.values());
        return mav;
    }

    @ResponseBody
    @RequestMapping(value="/getDbNameTableCountMap", method= RequestMethod.GET)
    public Map<String, Integer> getDbNameTableCountMap(Integer type){
        return dbNameTbNameMap.get(GatherTypeEnum.valueOf(type));
    }

    @ResponseBody
    @RequestMapping(value="/getWorkerState", method= RequestMethod.GET)
    public WorkerState getWorkerState() {
        return newWorkerState();
    }

    @ResponseBody
    @RequestMapping(value="/start", method= RequestMethod.GET )
    public WorkerState start(Integer type, String db, String from, String to) {
        Date fromDate = convertToDate(from);
        Date toDate = convertToDate(to);
        if(db == null || "0".equals(db)) {
            db =  Constants.ALL_DB;
        }

        GatherTypeEnum gatherTypeEnum = GatherTypeEnum.valueOf(type);
        String tbName = "";
        if (gatherTypeEnum.getId() == 1){
            tbName = "tb_sku";
        } else if (gatherTypeEnum.getId() == 2) {
            tbName = "tb_product";
        }

        if (Constants.ALL_DB.equals(db)){
            for (GatherTypeEnum gather : GatherTypeEnum.values()){
                for (String dbKey : dbNameTbNameMap.get(gather).keySet()){
                    log.info("In WorkerController.start all, from=" + String.valueOf(fromDate)
                            + ", to=" + String.valueOf(toDate) + ", db=" + dbKey + ", gatherType=" + gatherTypeEnum.getId());
                    // TODO: 2020/5/3 多个时需要再加一层worker，或是等待
                    while (gatherWorker.isRunning()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    gatherWorker.startGather(dbKey, tbName, fromDate, toDate);
                }
            }
        }else{
            log.info("In WorkerController.start, from=" + String.valueOf(fromDate)
                    + ", to=" + String.valueOf(toDate) + ", db=" + db + ", gatherType=" + gatherTypeEnum.getId());
            gatherWorker.startGather(db, tbName, fromDate, toDate);
        }
        return newWorkerState();
    }

    @ResponseBody
    @RequestMapping(value="/stop", method= RequestMethod.GET)
    public WorkerState stop() {
        gatherWorker.close();
        return newWorkerState();
    }

    @ResponseBody
    @RequestMapping(value="/reindex", method= RequestMethod.GET)
    public WorkerState reindex(Integer type, String dbName, String tbName, String ids) {
        log.info("In WorkerController.reindex, type=" + String.valueOf(type) + " dbName=" + dbName + " tbName=" + tbName + " ids=" + ids);
        GatherTypeEnum gatherTypeEnum = GatherTypeEnum.valueOf(type);
        String[] productSkuIds = ids.split(",");
        for (int i = 0; i < productSkuIds.length; i++) {
            try {
                indexWorker.index(gatherTypeEnum, dbName, tbName, productSkuIds[i].trim());
            } catch (Exception e) {
                log.error(String.format("Reindex failed, type:%d, db:%s, tableNo:%d, id:%s", type, dbName, tbName, productSkuIds[i].trim()) + e.toString());
            }
        }
        return newWorkerState();
    }

    @ResponseBody
    @RequestMapping(value="delete", method= RequestMethod.GET)
    public WorkerState deleteIndex(Integer type, String ids) {
        log.info("In WorkerController.deleteIndex, type=" + String.valueOf(type) + ", ids=" + ids);

        String[] deleteIds = ids.split(",");
        for (int i = 0; i < deleteIds.length; i++) {
            deleteIds[i] = deleteIds[i].trim();
        }
        GatherTypeEnum gatherTypeEnum = GatherTypeEnum.valueOf(type);
        indexWorker.delete(gatherTypeEnum, deleteIds);
        return newWorkerState();
    }

    private WorkerState newWorkerState() {
        return new WorkerState(gatherWorker.getWorkCount());
    }

    private Date convertToDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat dateTimeFormat;
            if (dateStr.contains(":")) {
                dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            } else {
                dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
            }

            return dateTimeFormat.parse(dateStr);
        } catch (Exception e) {
            log.error("In WorkerController, parse date failed! dateStr=" + dateStr);
        }

        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Integer> sku = new HashMap<>();
        sku.put("self1", 4);
        sku.put("self2", 4);
        Map<String, Integer> product = new HashMap<>();
        product.put("pop1", 4);
        product.put("pop2", 4);
        dbNameTbNameMap.put(GatherTypeEnum.valueOf(1), sku);
        dbNameTbNameMap.put(GatherTypeEnum.valueOf(2), product);
    }

    public static class WorkerState {
        private long taskCount;

        public WorkerState(long taskCount) {
            this.taskCount = taskCount;
        }
        public long getTaskCount() {
            return taskCount;
        }
    }
}