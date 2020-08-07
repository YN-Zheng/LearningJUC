package collections.blockingqueue;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * ArrayBlockingQueue:   有界;指定容量;公平:指定公平的话,等待最长时间的线程会优先处理
 * LinkedBlockingQueue: 无界;容量Integer.MAX_VALUE;内部结果:Node,两把锁.分析put方法
 * 并发容器总结:
 * concurrent包提供的容器分为三类:
 * 1. Concurrent*:大部分通过CAS实现并发
 * 2. CopyOnWrite*:通过复制一份原数据
 * 3. Blocking*:通过Reentrantlock(AQS)实现的
 */
public class LinkedBlockingQueueDemo {
    static final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
}
