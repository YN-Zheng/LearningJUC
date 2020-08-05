package atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 原子数组的使用方法
 */
public class AtomicArrayDemo {
    public static void main(String[] args) throws InterruptedException {
        AtomicIntegerArray array = new AtomicIntegerArray(1000);
        Thread[] threadsIncrementr = new Thread[100];
        Thread[] threadsDecrementr = new Thread[100];
        Runnable increment = new Increment(array);
        Runnable decrement = new Decrementer(array);

        for (int i = 0; i < 100; i++) {
            threadsIncrementr[i] = new Thread(increment);
            threadsDecrementr[i] = new Thread(decrement);
            threadsDecrementr[i].start();
            threadsIncrementr[i].start();
        }

        for (int i = 0; i < 100; i++) {
            threadsDecrementr[i].join();
            threadsIncrementr[i].join();
        }

        for (int i = 0; i < array.length(); i++) {
            if (array.get(i) != 0) {
                System.out.println("发现了错误" + i);

            }
        }
        System.out.println("运行结束");


    }
}


class Increment implements Runnable {


    private final AtomicIntegerArray array;

    public Increment(AtomicIntegerArray array) {
        this.array = array;
    }

    @Override
    public void run() {
        for (int i = 0; i < array.length(); i++) {
            array.getAndIncrement(i);
        }
    }
}

class Decrementer implements Runnable {

    private final AtomicIntegerArray array;

    public Decrementer(AtomicIntegerArray array) {
        this.array = array;
    }

    @Override
    public void run() {
        for (int i = 0; i < array.length(); i++) {
            array.getAndDecrement(i);
        }
    }
}
