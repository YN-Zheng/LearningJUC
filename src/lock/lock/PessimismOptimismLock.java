package lock.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 */
public class PessimismOptimismLock {

    int a;
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.incrementAndGet();
    }

    public synchronized void testMethod(){
        a++;
    }

}
