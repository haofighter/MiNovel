package com.hao.minovel.moudle.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 一个简答的线程池处理类
 */
public class SimplePoolExecutor {
    //核心线程和最大线程都是cpu核心数+1
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int MAX_CORE_POOL_SIZE = CPU_COUNT + 1;

    //线程存活时间
    private static final int SURPLUS_THREAD_LIFE = 30;

    private SimplePoolExecutor() {
    }

    public static ThreadPoolExecutor getnewInstance(int corePoolSize) {
        if (corePoolSize == 0) {
            return null;
        }
        corePoolSize = Math.min(corePoolSize, MAX_CORE_POOL_SIZE);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize,
                corePoolSize, SURPLUS_THREAD_LIFE, TimeUnit.SECONDS, new
                ArrayBlockingQueue<Runnable>(64), factory);
        //核心线程也会被销毁
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    private static ThreadFactory factory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    };

    static ThreadPoolExecutor executor;


    public static ThreadPoolExecutor getInstance() {
        return getInstance(MAX_CORE_POOL_SIZE);
    }

    public static ThreadPoolExecutor getInstance(int corePoolSize) {
        if (executor == null) {
            if (corePoolSize == 0) {
                return null;
            }
            corePoolSize = Math.min(corePoolSize, MAX_CORE_POOL_SIZE);
            executor = new ThreadPoolExecutor(corePoolSize,
                    corePoolSize, SURPLUS_THREAD_LIFE, TimeUnit.SECONDS, new
                    ArrayBlockingQueue<Runnable>(64), factory);
            //核心线程也会被销毁
            executor.allowCoreThreadTimeOut(true);
        }
        return executor;
    }
}
