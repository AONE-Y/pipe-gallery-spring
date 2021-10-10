package com.hainu.common.guard;

import java.util.concurrent.TimeoutException;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.guard
 * @Date：2021/10/10 10:05
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public class GuardObject {
    private Object response;

    public synchronized Object get(long timeout) throws TimeoutException{
        long begin = System.currentTimeMillis();
        long passTime=0;
        while (response == null) {
            long waitTime=timeout-passTime;
            if (waitTime<=0) {
                throw new TimeoutException("操作超时");

            }
            try {
                this.wait(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            passTime=System.currentTimeMillis()-begin;
        }
        return response;
    }

    public synchronized void complete(Object response){
        this.response = response;
        this.notifyAll();
    }
}
