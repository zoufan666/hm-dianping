package com.hmdp.utils;

public interface Ilock {
    /**
     * 尝试获取锁
     * @param timeoutSec
     * @return true代表成功，false代表获取锁失败
     */
    boolean tryLock(long timeoutSec);

    /**
     * 释放锁
     */
    void unLock();
}
