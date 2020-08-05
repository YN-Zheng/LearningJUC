package cas;

/**
 * 模拟CAS操作
 * 等价代码
 * CAS缺点:
 * 1. ABA问题:仅检查相等,无法判断是否被修改了 解决方法:增加版本号(类似数据库)
 * 2. 自旋时间过长
 * 应用场景: 乐观锁, 并发容器, 原子类
 */
public class SimulatedCAS {
    private volatile int value;

    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;
        if (oldValue == expectedValue) {
            value = newValue;
        }
        return oldValue;
    }
}
