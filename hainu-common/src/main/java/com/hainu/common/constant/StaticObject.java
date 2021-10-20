package com.hainu.common.constant;

import com.hainu.common.guard.GuardObject;
import com.hainu.common.queue.MessageQueue;

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
public class StaticObject {

    private static  Map<String, Byte> options =new HashMap<>();
    private static  Map<Integer, Byte> swChangeValue=new HashMap<>();
    private static GuardObject guardObject;
    private static MessageQueue messageQueue;

    public static Map<String, Byte> getOptions() {
        return options;
    }

    public static void setOptions(Map<String, Byte> options) {
        StaticObject.options = options;
    }

    public static Map<Integer, Byte> getSwChangeValue() {
        return swChangeValue;
    }

    public static void setSwChangeValue(Map<Integer, Byte> swChangeValue) {
        StaticObject.swChangeValue = swChangeValue;
    }

    public static GuardObject getGuardObject() {
        return guardObject;
    }

    public static void setGuardObject(GuardObject guardObject) {
        StaticObject.guardObject = guardObject;
    }

    public static MessageQueue getMessageQueue() {
        return messageQueue;
    }

    public static void setMessageQueue(MessageQueue messageQueue) {
        StaticObject.messageQueue = messageQueue;
    }

    static {
         options.put("deviceTemp", (byte) 0x01);
         options.put("deviceHumi", (byte) 0x02);
         options.put("deviceO2", (byte) 0x03);
         options.put("deviceGas", (byte) 0x04);
         options.put("deviceLlv", (byte) 0x05);
         options.put("deviceSmoke", (byte) 0x06);
        options.put("deviceInfra", (byte) 0xa6);
        options.put("deviceManhole", (byte) 0xa1);
        options.put("deviceLighting", (byte) 0xa2);
        options.put("deviceWaterpump", (byte) 0xa4);
        options.put("deviceFan", (byte) 0xa3);

        swChangeValue.put(1,(byte)0xff);
        swChangeValue.put(0,(byte)0x00);
    }
}
