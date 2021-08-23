package com.hainu.system.util;



import org.apache.commons.lang3.time.DateFormatUtils;


import java.util.Date;

/**
 * 日期时间工具类
 * @author AONE
 *
 */
public class DateUtil {

    /**
     * 格式化时间（yyyy-MM-dd HH:mm:ss）
     * @param date Date对象
     * @return 格式化后的时间
     */
    public static String formatTime(Date date) {
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }

}
