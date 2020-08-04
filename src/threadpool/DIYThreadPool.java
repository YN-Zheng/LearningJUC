package threadpool;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * 手动设置
 * 线程池里的线程数量设置为多少比较合适？
 * - CPU密集型：CPU核心数的1-2倍左右
 * - 耗时IO型：以JVM线程监控显示繁忙情况为根据
 * - 参考公式：线程数 = CPU核心数*(1+平均等待时间/平均工作时间）
 */
public class DIYThreadPool {
    int threadDIY = 10;
    int coreDIY = 10;
    int maxDIY = 100;
    private static final long DEFAULT_KEEPALIVE_MILLIS = 10L;

    ExecutorService newSingleThreadExecutor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    ExecutorService newFixedThreadPool = new ThreadPoolExecutor(threadDIY, threadDIY,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    ExecutorService newCachedThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

//    ExecutorService newScheduledThreadPool = new ThreadPoolExecutor(coreDIY, Integer.MAX_VALUE,
//            DEFAULT_KEEPALIVE_MILLIS, MILLISECONDS, new ScheduledThreadPoolExecutor.DelayedWorkQueue());
}
