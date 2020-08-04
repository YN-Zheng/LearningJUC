package threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 关闭线程池相关的五种方法
 */
public class ShutDown {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            threadPool.execute(new ShutDownTask());
        }
        Thread.sleep(1500);
/*
        List<Runnable> runnables = threadPool.shutdownNow();// 中断运行中的线程；直接返回队列中等待的线程；
        System.out.println(runnables.size());
*/
/*
        boolean b = threadPool.awaitTermination(20L, TimeUnit.SECONDS);
        System.out.println(b);*/

        threadPool.shutdown();
        threadPool.awaitTermination(20L, TimeUnit.SECONDS);
        System.out.println("being shutdown? " + threadPool.isShutdown()); // 是否进入停止状态（不一定完全结束）
        System.out.println("terminated? " + threadPool.isTerminated());  // 是否完全停止
        threadPool.execute(new ShutDownTask()); // 将会被拒绝
        Thread.sleep(10000);
        System.out.println("terminated? " + threadPool.isTerminated());  // 是否完全停止

    }

}

class ShutDownTask implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName());
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "被中断了");
        }
    }
}
