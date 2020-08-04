package lock.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 主要方法介绍
 * 1. tryLock(long time, TimeUnit unit): 超时就放弃
 * 2. lockInterruptibly(): 相当于tryLock(long time, TimeUnit unit) 将超时时间设为无线.等待锁的过程中, 线程可以被中断.
 * 3. unlock 解锁. 一定要放在finally中
 * 一种可中断锁的体现
 */
public class LockInterrupting implements Runnable {
    private Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        LockInterrupting r1 = new LockInterrupting();
        Thread t0 = new Thread(r1);
        Thread t1 = new Thread(r1);
        t0.start();
        t1.start();
        Thread.sleep(2000);
        t0.interrupt();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "尝试获取锁");
        try {
            lock.lockInterruptibly();
            try {
                System.out.println(Thread.currentThread().getName() + "获取到了锁");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "睡眠期间被中断了");
            } finally {
                System.out.println(Thread.currentThread().getName() + "释放了锁");
                lock.unlock();
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "获得锁期间被中断了");
        }
    }
}
