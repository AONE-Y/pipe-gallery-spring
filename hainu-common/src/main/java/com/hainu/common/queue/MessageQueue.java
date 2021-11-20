package com.hainu.common.queue;

import cn.hutool.log.StaticLog;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.queue
 * @Date：2021/10/12 10:26
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public class MessageQueue {
    private ReentrantLock lock = new ReentrantLock();
    private ConcurrentHashMap<String, Condition> conditions = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Object> lists = new ConcurrentHashMap();
    private final int capacity;
    private String CAP = "cap";

    public MessageQueue(int capacity) {
        this.capacity = capacity;
        this.conditions.put(CAP, lock.newCondition());
    }

    public Object take(long timeout, String flag) throws TimeoutException {
        if (conditions.containsKey(flag)) {
            return null;
        }
        lock.lock();
        try {
            long begin = System.currentTimeMillis();
            long passTime = 0;
            while (lists.get(flag) == null) {
                long waitTime = timeout - passTime;
                if (waitTime <= 0) {
                    throw new TimeoutException("操作超时");

                }
                Condition condition = this.lock.newCondition();
                conditions.put(flag, condition);
                try {
                    condition.await(waitTime, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                conditions.remove(flag);
                passTime = System.currentTimeMillis() - begin;
            }
            Object object = lists.remove(flag);
            conditions.get(CAP).signal();
            return object;
        } finally {
            lock.unlock();
        }


    }

    public void put(Object object, String flag) {
        lock.lock();
        try {
            while (lists.size() == capacity) {
                try {
                    conditions.get(CAP).await();
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }

            Condition condition = conditions.get(flag);
            if (condition != null) {
                lists.put(flag, object);
                conditions.get(flag).signal();
            } else {
                StaticLog.warn("还没有请求数据......");
            }
        } finally {
            lock.unlock();
        }
    }
}
