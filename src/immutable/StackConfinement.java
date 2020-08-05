package immutable;

/**
 * 栈封闭--将变量写在线程内部
 * <p>
 * 线程封闭的一种情况---栈封闭:在方法中新建的局部变量,实际上是存储在每个线程私有的栈空间,而每个线程的栈空间是不能被其他线程所访问到的,
 * 所以不会有线程安全问题.
 */
public class StackConfinement implements Runnable {
    int index = 0;

    public static void main(String[] args) throws InterruptedException {
        StackConfinement r1 = new StackConfinement();
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r1);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(r1.index);
    }

    public void inThread() {
        int neverGoOut = 0;
        for (int i = 0; i < 10000; i++) {
            neverGoOut++;
        }
        System.out.println("栈内保护的数字是线程安全的" + neverGoOut);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            index++;
        }
        inThread();
    }
}
