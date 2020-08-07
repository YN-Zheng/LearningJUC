package collections.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 阻塞队列:具有阻塞功能的队列
 * 阻塞功能:
 * take()获取并移除队列的头结点;若此时队列里无数据,则阻塞,知道队列里有数据
 * put()插入元素.但是如果队列已满,那么就无法继续插入,则阻塞,直到队列里有了空闲空间
 * 是否有界
 * 阻塞队列和线程池的关系:重要的组成部分
 */
public class ArrayBlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        Thread t1 = new Thread(new Interviewer(queue));
        Thread t2 = new Thread(new Consumer(queue));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("结束");
    }
}

class Interviewer implements Runnable {

    BlockingQueue<String> queue;

    public Interviewer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("10个候选人都来啦");
        for (int i = 0; i < 10; i++) {
            String candidate = "Candidate" + i;
            try {
                queue.put(candidate);
                System.out.println("安排好了" + candidate);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            queue.put("stop");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        String msg;
        try {
            while (!(msg = queue.take()).equals("stop")) {
                System.out.println(msg + "到了");
            }
            System.out.println("结束啦");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}