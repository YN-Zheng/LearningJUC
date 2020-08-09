package future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 演示FutureTask获取Future和任务的结果
 * Future的注意点
 * 1. for循环批量获取future的结果时,容易发生一部分线程很慢的情况,get方法调用时应该使用timeout限制。
 * 2. 生命周期不能后退
 * - 生命周期不能前进,不能后退。就和线程池的生命周期一样，一旦完成了任务，它就永久停在了“已完成”的状态，不能重头再来。
 *
 */
public class FutureTaskDemo {
    public static void main(String[] args) {
        Task task = new Task();
        FutureTask<Integer> integerFutureTask = new FutureTask<>(task);
//        new Thread(integerFutureTask).start();
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(integerFutureTask);
        try {
            System.out.println("integerFutureTask.get() = " + integerFutureTask.get());
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}


class Task implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("子线程正在计算");
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        return sum;
    }
}