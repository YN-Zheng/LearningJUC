package imooccache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import imooccache.computable.Computable;
import imooccache.computable.MayFail;

/**
 * 出于安全性考虑,缓存需要设置有效期,到期自动失效
 * 随机过期时间,防止同时过期导致雪崩
 */
public class ImoocCache10<A, V> implements Computable<A, V> {

    public final static ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public ImoocCache10(Computable<A, V> c) {
        this.c = c;
    }

    public static void main(String[] args) throws Exception {
        ImoocCache10<String, Integer> expensiveComputer = new ImoocCache10<>(new MayFail());
        for (int i = 0; i < 10; i++) {
            Integer times = i;
            String finalI = i % 3 + "";
            Thread.sleep(1500);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Integer result = expensiveComputer.computeRandomExpire(finalI);
                        System.out.println("第" + times + "次计算结果" + result);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public V computeRandomExpire(A arg) throws InterruptedException {
        long randomExpire = (long) (Math.random() * 10000);
        return compute(arg, randomExpire);
    }

    public V compute(A arg, long expire) throws InterruptedException {
        if (expire > 0) {
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    expire(arg);
                }
            }, expire, TimeUnit.MILLISECONDS);
        }
        return compute(arg);
    }

    public synchronized void expire(A key) {
        Future<V> future = cache.get(key);
        if (future != null) {
            if (!future.isDone()) {
                System.out.println(key + " Future任务被取消");
                future.cancel(true);
            }
            System.out.println(key + "过期时间到,缓存被清除");
            cache.remove(key);
        }
    }

    @Override
    public V compute(A arg) throws InterruptedException, CancellationException {
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
            /*else {
                System.out.println("曾经调用过相同参数 " + arg + " 的计算,等待计算结果,一并返回");
            }*/
            try {
                return f.get();
            }
            catch (CancellationException e) {
                cache.remove(arg);
                System.out.println(arg + " 被取消了");
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
