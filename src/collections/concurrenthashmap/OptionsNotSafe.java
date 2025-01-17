package collections.concurrenthashmap;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 组合操作并不保证线程安全
 * 使用replace 或 putIfAbsent等
 */
public class OptionsNotSafe implements Runnable {
    private static final ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        scores.put("小明", 0);
        Thread t1 = new Thread(new OptionsNotSafe());
        Thread t2 = new Thread(new OptionsNotSafe());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(scores);

    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            Integer score = scores.get("小明");
            while (!scores.replace("小明", score, score + 1)) {

            }
        }
    }
}
