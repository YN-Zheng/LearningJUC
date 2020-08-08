package flowcontrol.countdownlatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模拟100米跑步,5名同学都准备好了,等待裁判命令
 * 一等多: 同时跑步
 * 多等一: 所有人到达后停止比赛
 * 不能回滚重用
 */
public class countDownLatchDemo1And2 {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    System.out.println("准备完毕,等待发令枪");
                    try {
                        begin.await();
                        System.out.println(no + "开始跑步了");
                        Thread.sleep(new Random().nextInt(10000));
                        System.out.println(no + "到达终点了");
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        end.countDown();
                    }
                }
            };
            executorService.execute(r);
        }
        // 裁判员检查场地
        Thread.sleep(5000);
        System.out.println("发令枪响,比赛开始");
        begin.countDown();
        end.await();
        System.out.println("结束");
    }
}
