package future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 演示get的超时方法
 * 注意超时后处理,调用future.cancel()。演示cancel传入true和false的区别，代表是否中断正在执行的任务。
 * cancel方法:取消任务的执行
 * 1. 如果这任务还没有开始执行：任务被取消，未来也不会执行，返回true
 * 2. 如果任务已完成或已取消：那么cancel方法会执行失败，方法返回false
 * 3. 如果这个任务已经开始执行，那么取消方法不会直接取消该任务，而是会根据我们填的参数mayInterruptIfRunning做判断
 * Future.cancel(true)适用于:
 * 1. 任务能够interrupt
 * Future.cancel(false)仅用于避免启动尚未启动的任务:
 * 1.未能处理interrupt的任务
 * 2.不清楚任务是否支持取消
 * 3. 需要等待已经开始的任务完成
 */
public class TimeOut {

    public static final ExecutorService service = Executors.newFixedThreadPool(10);
    private static final Ad DEFAULT_AD = new Ad("无网广告");

    public static void main(String[] args) {
        TimeOut timeOut = new TimeOut();
        timeOut.printAd();
    }

    public void printAd() {
        Future<Ad> f = service.submit(new FetchAdTask());
        Ad ad;
        try {
            ad = f.get(2000, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            ad = new Ad("被中断广告");
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            ad = new Ad("异常广告");
            e.printStackTrace();
        }
        catch (TimeoutException e) {
            ad = new Ad("超时广告");
            System.out.println("超时,为获取到广告");
            boolean cancel = f.cancel(true); // 对此时返回的不感兴趣

            System.out.println("cancel = " + cancel);
        }
        service.shutdown();
        System.out.println(ad);
    }

    static class FetchAdTask implements Callable<Ad> {

        @Override
        public Ad call() throws Exception {
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e) {
                System.out.println("sleep期间被中断了");
                return new Ad("被中断广告");
            }
            return new Ad("旅游订票哪家强?找某程");
        }
    }

    static class Ad {
        String name;

        public Ad(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Ad{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

}
