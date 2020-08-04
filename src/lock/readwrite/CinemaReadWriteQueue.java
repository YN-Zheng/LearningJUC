package lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 演示读不能插队的情况
 */
public class CinemaReadWriteQueue {
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);

    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static void main(String[] args) {
        new Thread(CinemaReadWriteQueue::read, "Thread1").start();
        new Thread(() -> write(), "Thread2").start();
        new Thread(() -> write(), "Thread3").start();
        new Thread(() -> read(), "Thread4").start();
        new Thread(() -> read(), "Thread5").start();
        new Thread(() -> write(), "Thread6").start();
        new Thread(() -> write(), "Thread7").start();
    }

    private static void read() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到了读锁，正在读取");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放了读锁");
            readLock.unlock();
        }
    }

    private static void write() {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到了写锁，正在写");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放了写锁");
            writeLock.unlock();
        }
    }
}
