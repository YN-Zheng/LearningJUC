package flowcontrol.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模拟100米跑步,5名同学都准备好了,等待裁判命令,同时跑步
 * 多等一的场景
 */
public class CountDownLatchDemo2 {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    System.out.println("准备完毕,等待发令枪");
                    try {
                        latch.await();
                        System.out.println(no + "开始跑步了");
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            executorService.execute(r);
        }
        // 裁判员检查场地
        Thread.sleep(5000);
        System.out.println("发令枪响,比赛开始");
        latch.countDown();
    }
}
