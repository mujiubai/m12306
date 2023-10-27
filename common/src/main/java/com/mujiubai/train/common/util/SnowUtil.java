package com.mujiubai.train.common.util;

import cn.hutool.core.util.IdUtil;

public class SnowUtil {
    private static long workerID=1;
    private static long dataCenterId=1;

    public static Long getSnowFlakeNextId(){
        return IdUtil.getSnowflake(workerID, dataCenterId).nextId();
    }

    public static String getSnowFlakeNextIdStr(){
        return IdUtil.getSnowflake(workerID, dataCenterId).nextIdStr();
    }
}
