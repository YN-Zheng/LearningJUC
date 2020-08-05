package atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 演示高并发环境下, LongAdder比AtomicLong性能好
 * 原理:空间换时间
 * AtomicLong统一计数。每一次++时,会flush和refresh自己的局部cache, 同步公共的counter(JMM)
 * LongAdder分段累加。每个线程有一个自己的线程内计数器，不需要同步
 * 1. base变量:竞争不激烈,直接累加到该变量上
 * 2. Cell[]数组:竞争激烈, 各个线程分散累加到自己的槽Cell[i]中
 * 3. 特点:sum()不加锁, 所以求和可能不精确
 * 总结:
 * 低征用下,效率相似;竞争激烈时, LongAdder的预期吞吐量高得多, 但要消耗更多空间
 * LongAdder适用统计求和场景, 只提供add方法,而AtomicLong还具有CAS方法
 */
public class AtomicLongDemo {
    public static void main(String[] args) {
        // AtomicLong
        AtomicLong counter = new AtomicLong(0);
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            threadPool.submit(new AtomicLongTask(counter));
        }
        threadPool.shutdown();
        while (!threadPool.isTerminated()) ;
        long end = System.currentTimeMillis();
        System.out.println(counter.get());
        System.out.println("AtomicLong耗时:\t" + (end - start));

        //LongAdder
        LongAdder adder = new LongAdder();
        threadPool = Executors.newFixedThreadPool(20);
        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            threadPool.submit(new LongAdderTask(adder));
        }
        threadPool.shutdown();
        while (!threadPool.isTerminated()) ;
        end = System.currentTimeMillis();
        System.out.println(adder.sum());
        System.out.println("LongAdder耗时:\t" + (end - start));

    }


    private static class AtomicLongTask implements Runnable {
        private final AtomicLong counter;


        public AtomicLongTask(AtomicLong counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                counter.incrementAndGet();
            }
        }
    }

    private static class LongAdderTask implements Runnable {
        private final LongAdder counter;


        public LongAdderTask(LongAdder counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        }
    }
}
