package com.jd.worker.index;

import com.jd.enums.GatherTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class IndexWorker {
    public boolean index(GatherTypeEnum gatherTypeEnum, String dbName, String tbName, String productSkuId){
        log.info("IndexWorker index GatherTypeEnum=" + gatherTypeEnum + ", [" + dbName + "," + tbName + "], productSkuId=" + productSkuId);
        return true;
    }

    public boolean delete(GatherTypeEnum gatherTypeEnum, String[] deleteIds){
        log.info("IndexWorker delete GatherTypeEnum=" + gatherTypeEnum + ", deleteIds=" + Arrays.toString(deleteIds));
        return true;
    }
}