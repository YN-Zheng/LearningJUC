package imooccache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import imooccache.computable.ExpensiveFunction;

/**
 * 测试缓存效果
 *
 * 利用ThreadLocal获取dateformater,打印当前时间,证明countDownLatch保证了同时放行
 */
public class ImoocCache11 {
    static ImoocCache10<String, Integer> expensiveComputer = new ImoocCache10<>(new ExpensiveFunction());
    static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            service.submit(() -> {
                Integer result = null;
                try {
                    System.out.println("开始等待");
                    countDownLatch.await();
                    SimpleDateFormat simpleDateFormat = ThreadSafeFormatter.dateFormatThreadLocal.get();
                    String time = simpleDateFormat.format(new Date());
                    System.out.println(Thread.currentThread().getName() + "  " + time + "被放行");
                    result = expensiveComputer.compute("666");
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(finalI + "   " + result);
            });
        }
        Thread.sleep(5000);
        countDownLatch.countDown();
        long start = System.currentTimeMillis();
        service.shutdown();
        while (!service.isTerminated()) {

        }
        System.out.println("总耗时: " + (System.currentTimeMillis() - start));
    }
}

class ThreadSafeFormatter {
    public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = new ThreadLocal<>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
    };

    //lambda表达式写法
    public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal2 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

}