package threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 利用线程池
 * 1000个线程分别打印出生日日期
 */
public class ThreadLocalNormalUsage02 {
    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public String date(int seconds) {
        // 参数单位是毫秒,从1970.1.1 00:00:00 GMT计时
        Date date = new Date(1000L * seconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) {


        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            threadPool.submit(new Thread(new Runnable() {
                @Override
                public void run() {
                    String date = new ThreadLocalNormalUsage02().date(finalI);
                    System.out.println(Thread.currentThread().getName() + ":" + date);
                }
            }));
        }
        threadPool.shutdown();

    }

}
