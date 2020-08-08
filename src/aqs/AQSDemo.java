package aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO
 */
public class AQSDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        ReentrantLock lock = new ReentrantLock();
    }
}
