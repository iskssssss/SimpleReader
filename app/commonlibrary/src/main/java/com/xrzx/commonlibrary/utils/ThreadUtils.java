package com.xrzx.commonlibrary.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 线程工具类
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class ThreadUtils {

    private static volatile ThreadPoolExecutor crawlingExecutorServiceThreadPool = null;

    private static volatile ThreadPoolExecutor otherExecutorServiceThreadPool = null;

    /**
     * 获取线程池
     *
     * @param nThreads 线程数
     * @return ExecutorService
     */
    private static ThreadPoolExecutor newFixedThreadPool(int nThreads) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 获取爬行执行器服务线程池w
     *
     * @return
     */
    public static ThreadPoolExecutor getCrawlingExecutorServiceThreadPool() {
        if (null == crawlingExecutorServiceThreadPool) {
            synchronized (ThreadPoolExecutor.class) {
                if (null == crawlingExecutorServiceThreadPool) {
                    crawlingExecutorServiceThreadPool = newFixedThreadPool(6);
                }
            }
        }
        return crawlingExecutorServiceThreadPool;
    }


    /**
     * 获取其他执行器服务线程池
     *
     * @return
     */
    public static ThreadPoolExecutor getOtherExecutorServiceThreadPool() {
        if (null == otherExecutorServiceThreadPool) {
            synchronized (ExecutorService.class) {
                if (null == otherExecutorServiceThreadPool) {
                    otherExecutorServiceThreadPool = newFixedThreadPool(3);
                }
            }
        }
        return otherExecutorServiceThreadPool;
    }
}
