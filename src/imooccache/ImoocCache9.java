package imooccache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import imooccache.computable.Computable;
import imooccache.computable.MayFail;

/**
 * 解决缓存污染问题。在异常处理语句中remove掉错误
 *
 */
public class ImoocCache9<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public ImoocCache9(Computable<A, V> c) {
        this.c = c;
    }

    public static void main(String[] args) throws Exception {
        ImoocCache9<String, Integer> expensiveComputer = new ImoocCache9<>(new MayFail());
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
    public V compute(A arg) throws InterruptedException {
        while (true) {
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
                System.out.println("从FutureTask调用了计算函数,计算  " + arg);
                ft.run();
            }
            else {
                System.out.println("曾经调用过相同参数 " + arg + " 的计算,等待计算结果,一并返回");
            }
            try {
                return f.get();
            }
            catch (CancellationException e) {
                cache.remove(arg);
                System.out.println("被取消了");
                throw e;
            }
            catch (InterruptedException e) {
                cache.remove(arg);
                System.out.println("被中断了");
                throw e;
            }
            catch (ExecutionException e) {
                cache.remove(arg);
                System.out.println(arg + "计算错误,需要重试");
            }
        }
    }
}
