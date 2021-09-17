package com.hainu.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.constant
 * @Date：2021/9/17 14:51
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public class SwReflect {

    public static  Map<String, Byte> swChange=new HashMap<>();
    public static  Map<Integer, Byte> swChangeValue=new HashMap<>();

     static {
        swChange.put("deviceSmoke", (byte) 0x80);
        swChange.put("deviceLighting", (byte) 0x81);
        swChange.put("deviceWaterpump", (byte) 0x82);
        swChange.put("deviceFan", (byte) 0x83);
        swChange.put("deviceInfra", (byte) 0x84);
        swChange.put("deviceGuard", (byte) 0x85);


        swChangeValue.put(1,(byte)0xff);
        swChangeValue.put(0,(byte)0x00);
    }
}
