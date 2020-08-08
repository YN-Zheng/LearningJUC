package flowcontrol.countdownlatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 工厂中,质检,5个工人检查,所有人都认为通过,才通过
 */
public class CountDownLatchDemo1 {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(new Random().nextInt(10000));
                        System.out.println(no + "完成了检查");
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        latch.countDown();
                    }
                }
            };
            service.submit(r);
        }
        System.out.println("等待五个人检查完...");
        latch.await();
        System.out.println("所有人都完成了工作,进入下一个环节");
    }
}
