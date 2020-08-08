package aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 自己用AQS实现一个简单的线程协作器
 */
public class OneShotLatch {
    private final Sync sync = new Sync();

    public static void main(String[] args) throws InterruptedException {
        OneShotLatch oneShotLatch = new OneShotLatch();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "尝试获取latch, 若获取失败则等待");
                    oneShotLatch.await();
                    System.out.println(Thread.currentThread().getName() + "开闸放行,继续运行");
                }
            }).start();
        }
        Thread.sleep(5000);
        oneShotLatch.signal();

    }

    public void await() {
        sync.acquireShared(0);
    }

    public void signal() {
        sync.releaseShared(0);
    }

    private class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected int tryAcquireShared(int arg) {
            // state==1:门栓打开, 跳过doAcquireShared(arg)
            return (getState() == 1) ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            // 打开门栓
            setState(1);
            return true;
        }
    }
}
