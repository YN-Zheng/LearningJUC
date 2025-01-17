## 线程池  

### 使用线程池

线程池减少了新建和销毁线程带来的开销。

Executors 中提供了设定好参数的线程池：

```java
// 线程数设置为1,使用LinkedBlockingQueue; 请求堆积,大量内存
ExecutorService newSingleThreadExecutor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

// 自定义线程数;队列没有容量上限,内存溢出OOM
ExecutorService newFixedThreadPool = new ThreadPoolExecutor(threadDIY, threadDIY,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

// 可能导致创建非常多的线程,导致OOM
ExecutorService newCachedThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

```

我们也可以自己DIY一个，需要指定定的参数有：

```java
    /**
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *        pool
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     * @param unit the time unit for the {@code keepAliveTime} argument
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue)
```

### 关闭线程池的相关方法

```java
pool.shutdown();// 中断线程,拒绝新任务
pool.shutdownNow();// 中断进行中的线程, 停止等待中的线程,并返回等待中的线程
pool.isShutdown();// 是否进入停止状态（不一定完全结束）
pool.isTerminated(); // 是否完全停止
boolean terminated = pool.awaitTermination(20L,TimeUnits.SECONDS);//等待,直到线程池中所有的线程都被停止了
```

