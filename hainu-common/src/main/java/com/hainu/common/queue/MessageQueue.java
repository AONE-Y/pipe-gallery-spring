package com.hainu.common.queue;

import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

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
    private LinkedList<Object> list=new LinkedList<>();

    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    public Object take(long timeout) throws TimeoutException{
        synchronized (list) {
            long begin = System.currentTimeMillis();
            long passTime=0;
            while (list.isEmpty()){
                long waitTime=timeout-passTime;
                if (waitTime<=0) {
                    throw new TimeoutException("操作超时");

                }
                try {
                    list.wait(waitTime);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passTime=System.currentTimeMillis()-begin;
            }
            Object object=list.removeFirst();
            list.notifyAll();
            return object;
        }
    }

    public void put(Object object){
        synchronized (list) {
            while (list.size()==capacity){
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            list.addLast(object);
            list.notifyAll();
        }
    }
}
