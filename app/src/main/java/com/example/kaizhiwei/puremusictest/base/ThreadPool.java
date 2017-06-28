package com.example.kaizhiwei.puremusictest.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {
    private static ExecutorService cachedThreadPool;
    private static byte[] ThreadPoolLock = new byte[] {};

    // 提交线程
    public static Future<?> submit(Runnable mRunnable) {
        if (cachedThreadPool == null) {
            synchronized (ThreadPoolLock) {
                cachedThreadPool = Executors.newCachedThreadPool();
            }
        }
        return cachedThreadPool.submit(mRunnable);
    }

    // 关闭
    public static void shutdown() {
        if (cachedThreadPool != null && !cachedThreadPool.isShutdown())
            cachedThreadPool.shutdown();
        cachedThreadPool = null;
    }
}
