# JUC网课学习笔记

线程池+各种锁+CAS+AQS+ThreadLocal+ConcurrentHashMap+并发综合实战项目



## 线程池 (CH3-CH4)

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



### ThreadLocal

1. 两个作用
   1. 让某个需要用到的对象在线程间隔离（每个线程都有自己的独立对象）
   2. 让任何方法中都可以轻松地获取到该对象

2. 两种场景

   1. initialValue: ThreadLocal第一次get的时候将对象初始化出来：初始化时机由我们控制；**线程不安全的工具类**（SimpleDateFormat）
   2. set: 对象生成时机不由我们控制（User对象）

3. 使用ThreadLocal的好处

   1. 达到线程安全
   2. 不需要加锁，提高执行效率
   3. 高效利用内存，节省开销
   4. 免去传参的繁琐。**低耦合**


4. 内存泄漏问题
    1. ThreadLocal中的Entry(**key是弱引用**,value是强引用)
    2. 若ThreadLocal不被使用了, key被GC回收, value不会; 调用rehash, remove等方法时会主动释放掉key=null的value
    3. 最佳实践(阿里): 主动调用remove



## 锁

### 锁的分类

| 分类依据                     |                    |                      |
| :--------------------------- | ------------------ | -------------------- |
| 是否锁住同步资源             | 悲观锁（互斥同步） | 乐观锁（非互斥同步） |
| 能否共享同一把锁             | 共享锁             | 独占锁               |
| 多线程竞争时，是否排队       | 排队：公平锁       | 先尝试插队：非公平锁 |
| 同一线程能否重复获取同一把锁 | 可重入锁           | 不可重入锁           |
| 是否可中断                   | 可中断锁           | 非可中断锁           |
| 等锁的过程                   | 自旋锁             | 阻塞锁               |

#### 乐观

- 实现：CAS算法

- 适用场景
  - **临界区小**，持锁时间短（时间越长，相比悲观锁的开销就越大）
  - **多读少写**

- 典型例子
  - 原子类
  - 并发容器
  - synchronized锁优化后的（轻量级锁
  - GIT
  - 数据库（version控制数据库？？？TODO）

#### 可重入锁

源码分析

- 获取锁时先判断。若当前线程就是已经占有锁的线程，则status+1
- 释放锁时先判断是否当前线程持有锁，然后判断status。若status=0，才真正释放锁。



#### 非公平锁

- 插队以提高效率：吞吐量更大；**线程饥饿**问题

- 避免唤醒带来的**空档期**

- **tryLock**不遵守公平，自带插队属性



#### 共享锁和排他锁

- 解决痛点：多读场景没有线程安全问题

- 排他锁：独占锁、独享锁
- 共享锁：又称为读锁。可以查看但无法修改和删除数据

- 插队？
  - 公平锁：不允许插队
  - 非公平锁
    - 写锁可以随时插队 
    - 读锁仅在：等待队列头节点不是想获取写锁的时候，可以插队

```java
// JUC 中的读写锁
ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();


// 升降级策略：只能降级，不能升级
writeLock.lock();
try {
    readLock.lock();  //"在不释放写锁的情况下，直接获取读锁，成功降级");
} catch (InterruptedException e) {
    e.printStackTrace();
} finally {
    readLock.unlock();
    writeLock.unlock();
}
```







### synchronized关键字

缺点

1. 试图获得锁不能设定超时
2. 不能中断一个正在试图获得锁的线程
3. 不够灵活（相比：读写锁），加锁和释放锁的时机单一



### Lock接口

特点

- 不会像synchronized一样异常时释放锁（最佳实践：finally中释放锁）

- **可见性保证**：和synchronized有同样的内存语意。下一个线程加锁后可以看见所有前一个线程解锁前发生的所有操作

方法

```java
lock();
unlock(); //解锁
tryLock() //尝试获取锁, 若获取失败则立即返回
tryLock(long time, TimeUnit unit) //尝试获取锁, 一段时间后超时就放弃
lockInterruptibly() // 相当于无限时间的tryLock(long time, TimeUnit unit). 等待过程中,可以被中断
```



### JVM对锁的优化 [TODO]

#### synchronized原理

##### 偏向锁

##### 轻量锁

##### 重量锁

#### 锁消除

#### 锁粗化







### 优化策略

1. 缩小同步代码块
2. 尽量不要锁住方法
3. 减少锁的次数
4. 避免人为制造“热点”
5. 锁中尽量不要再包含锁
6. 选择合适的类型或合适的工具类





### [CH10：控制并发流程](src/flowcontrol/note.md)

### [CH11：AQS](src/aqs/note.md)






