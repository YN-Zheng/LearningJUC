package imooccache;

import java.util.HashMap;
import java.util.Map;

import imooccache.computable.Computable;
import imooccache.computable.ExpensiveFunction;

/**
 * 演示两种需要优化的情况:
 * 1. 分别计算1,2时,相互之间不需要同步
 * 2. 分别计算1,1时,不应该重复计算
 */
public class ImoocCache3<A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A, V> c;

    public ImoocCache3(Computable<A, V> c) {
        this.c = c;
    }

    public static void main(String[] args) throws Exception {
        ImoocCache3<String, Integer> expensiveComputer = new ImoocCache3<>(new ExpensiveFunction());
        for (int i = 0; i < 10; i++) {
            String finalI = i / 2 + "";
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
    public synchronized V compute(A arg) throws Exception {
        System.out.println("进入缓存机制");
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
