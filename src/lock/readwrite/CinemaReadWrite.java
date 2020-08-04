package lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 共享锁和排他锁总结
 * 1. ReentrantReadWriteLock实现了ReadWriteLock接口
 * 2. 锁申请和释放策略：多读，一写
 *    1. 多个线程只申请读锁，都可以申请到
 *    2. 若一个线程已经占用读锁，其他线程要申请写锁，则申请写锁的线程会一直等待释放读锁
 *    3. 有一个线程已经占用了写锁，其他申请写锁或读锁的线程都会等待
 * 3. 插队策略：读锁不能插队（防止饥饿）
 * 4. 升降级策略：只能降级，不能升级
 * 5. 适用场合：读多写少的情况，合理使用可以提高并发效率。
 */
public class CinemaReadWrite {
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static void main(String[] args) {
        new Thread(CinemaReadWrite::read,"Thread1").start();
        new Thread(()->read(),"Thread2").start();
        new Thread(()->write(),"Thread3").start();
        new Thread(()->write(),"Thread4").start();
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
