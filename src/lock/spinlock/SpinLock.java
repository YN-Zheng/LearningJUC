package lock.spinlock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁
 * atomic包下的类基本都是自旋锁的实现
 * 演示：自己写一个自旋锁
 * 1. 适用于多核服务器。并发数不高的情况下，减少了线程切换的花销，比阻塞所效率高
 * 2. 适用于临界区比较短小的情况（若临界区大，线程很久才会释放锁）
 */
public class SpinLock {
    private AtomicReference<Thread> sign = new AtomicReference<>();

    public void lock() {
        Thread current = Thread.currentThread();
        while (!sign.compareAndSet(null, current)) {
            System.out.println(current.getName() + "自旋获取失败，再次尝试");
        }
    }

    public void unlock() {
        Thread current = Thread.currentThread();
        sign.compareAndSet(current, null);
    }

    public static void main(String[] args) {
        SpinLock spinLock = new SpinLock();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "开始尝试获取自旋锁");
                spinLock.lock();
                System.out.println(Thread.currentThread().getName() + "获取到了自旋锁");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(Thread.currentThread().getName() + "释放了自旋锁");
                    spinLock.unlock();
                }
            }
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();

    }
}
