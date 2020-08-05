package atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 总结:原子类
 * <ol>
 * <li>什么是原子类, 有什么作用?</li>
 * <li>6类原子类概览
 * <ol>
 * <li>基本类型</li>
 * <li>数组类型</li>
 * <li>引用类型</li>
 * </ol>
 * </li>
 * <li>普通变量升级为原子类 AtomicIntegerFieldUpdater</li>
 * <li>Adder累加器</li>
 * <li>Accumulator 累加器(Adder的升级,不仅可以进行加操作,还可以进行其他数字运算)</li>
 * </ol>
 */
public class AtomicIntegerDemo1 {
    private static final AtomicInteger atomicInteger = new AtomicInteger();
    private static Integer basicInteger = 0;

    public static void increAtomic() {
        atomicInteger.incrementAndGet();
    }

    public static void increBasic() {
        basicInteger++;
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    increAtomic();
                    increBasic();
                }
            }
        };
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        System.out.println("atomicInteger = " + atomicInteger);
        System.out.println("basicInteger = " + basicInteger);
    }
}
