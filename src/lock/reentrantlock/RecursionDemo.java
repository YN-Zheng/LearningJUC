package lock.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 递归地获取锁
 */
public class RecursionDemo {
    private static ReentrantLock lock = new ReentrantLock();
    private static void accessResource(){
        lock.lock();
        try{
            System.out.println("已经对资源进行了处理, lock.getHoldCount= "+lock.getHoldCount());
            if(lock.getHoldCount()<5){
                accessResource();
            }
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        accessResource();
    }
}
