package lock.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程打印字符串
 * Reentrant的基本用法
 * 演示被打断
 */
public class LockDemo {

    public static void main(String[] args) {
        new LockDemo().init();
    }

    private void init() {
        final Outputer outputer = new Outputer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    outputer.output("悟空");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    outputer.output("大师兄");
                }
            }
        }).start();
    }

    static class Outputer {

        private static ReentrantLock lock = new ReentrantLock();

        // 字符串打印发方法,一个个字符的答应
        public void output(String name) {
            int len = name.length();
//            lock.lock();
            try {
                for (int i = 0; i < len; i++) {
                    System.out.print(name.charAt(i));
                }
                System.out.println("");
            } finally {
//                lock.unlock();
            }
        }
    }


}
