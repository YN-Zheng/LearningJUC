package imooccache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import imooccache.computable.Computable;
import imooccache.computable.ExpensiveFunction;

/**
 * 使用ConcurrentHashMap代替synchronized关键字
 * 解决了1,仍然存在重复计算的问题
 *
 */
public class ImoocCache5<A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public ImoocCache5(Computable<A, V> c) {
        this.c = c;
    }

    public static void main(String[] args) throws Exception {
        ImoocCache5<String, Integer> expensiveComputer = new ImoocCache5<>(new ExpensiveFunction());
        for (int i = 0; i < 10; i++) {
            String finalI = i + "";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Integer result = expensiveComputer.compute(finalI);
                        System.out.println("第" + finalI + "次计算结果" + result);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public V compute(A arg) throws Exception {
        System.out.println("进入缓存机制");
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
