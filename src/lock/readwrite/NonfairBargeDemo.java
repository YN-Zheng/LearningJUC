package lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读锁插队（线程1，2在读，写线程3在等，新来了读线程4？）
 * 策略1. 读可以插队，效率高；容易造成饥饿
 * 策略2. 避免饥饿；不允许插队 ✔
 * 公平锁：不允许插队
 * <p>
 * 非公平锁
 * 1. 写锁可以随时插队
 * 2。 读锁仅在等待队列头节点不是想获取写锁的时候可以等插队
 */
public class NonfairBargeDemo {
    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);
    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static void main(String[] args) {
        new Thread(()->write(),"Thread0").start();
        new Thread(()->read(),"Thread1").start();
        new Thread(()->read(),"Thread2").start();
        new Thread(()->write(),"Thread3").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread thread[] = new Thread[1000];
                for (int i = 0; i < 1000 ;i++){
                    thread[i] = new Thread(()->read(),"子线程船舰的Thread"+i);
                }
                for (int i = 0; i < 1000; i++) {
                    thread[i].start();
                }
            }
        }).start();
    }

    private static void read() {
        System.out.println(Thread.currentThread().getName()+"开始尝试获取读锁");
        readLock.lock();
        try{
            System.out.println(Thread.currentThread().getName()+"得到读锁，正在读取");
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName()+"释放读锁");
            readLock.unlock();
        }
    }
    private static void write() {
        System.out.println(Thread.currentThread().getName()+"开始尝试获取写锁");
        writeLock.lock();
        try{
            System.out.println(Thread.currentThread().getName()+"得到写锁，正在写取");
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName()+"释放写锁");
            writeLock.unlock();
        }
    }
}
