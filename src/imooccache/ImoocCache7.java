package imooccache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import imooccache.computable.Computable;
import imooccache.computable.ExpensiveFunction;

/**
 * 使用putIfAbsent这样的原子组合操作,进一步避免重复运算的可能性
 *
 */
public class ImoocCache7<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public ImoocCache7(Computable<A, V> c) {
        this.c = c;
    }

    public static void main(String[] args) throws Exception {
        ImoocCache7<String, Integer> expensiveComputer = new ImoocCache7<>(new ExpensiveFunction());
        for (int i = 0; i < 10; i++) {
            Integer times = i;
            String finalI = i % 3 + "";
            Thread.sleep(500);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Integer result = expensiveComputer.compute(finalI);
                        System.out.println("第" + times + "次计算结果" + result);
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

        //可能同时get到null,同时进入方法计算
        Future<V> f = cache.get(arg);
        if (f == null) {
            Callable<V> callable = new Callable<>() {
                @Override
                public V call() throws Exception {
                    return c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<>(callable);
            f = ft;
            cache.putIfAbsent(arg, ft);
            System.out.println("从FutureTask调用了计算函数");
            ft.run();
        }
        else {
            System.out.println("曾经调用过相同参数的计算,等待计算结果,一并返回");
        }
        return f.get();
    }
}
