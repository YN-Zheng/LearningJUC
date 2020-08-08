package future;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 演示批量提交任务时,用  List来批量接收结果
 */
public class MultiFutures {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Integer j = 0;
        ExecutorService service = Executors.newFixedThreadPool(2);


        ArrayList<Future> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Future<Integer> future = service.submit(new OneFuture.CallableTask());
            futures.add(future);
        }
        for (int i = 0; i < 20; i++) {
            System.out.println("futures.get(" + i + ").get() = " + futures.get(i).get());
        }
    }
}
