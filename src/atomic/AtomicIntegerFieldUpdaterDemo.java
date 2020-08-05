package atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 演示AtomicIntegerFieldUpdater的用法
 * 原理:反射;故必须可见
 * <p>
 * 不能是static
 */
public class AtomicIntegerFieldUpdaterDemo implements Runnable {
    public static AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");
    static Candidate tom;
    static Candidate peter;

    public static void main(String[] args) throws InterruptedException {
        Runnable r = new AtomicIntegerFieldUpdaterDemo();
        tom = new Candidate();
        peter = new Candidate();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("tom:" + tom.score);
        System.out.println("peter:" + peter.score);
    }

    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            peter.score++;
            scoreUpdater.getAndIncrement(tom);
        }
    }

    public static class Candidate {
        volatile int score;
    }
}
