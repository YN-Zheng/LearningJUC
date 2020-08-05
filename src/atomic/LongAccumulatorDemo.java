package atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

/**
 * 演示LongAccumulator的用法
 * 适用场景:大量计算;并行计算;顺序无要求
 */
public class LongAccumulatorDemo {
    public static void main(String[] args) {
        LongAccumulator accumulator = new LongAccumulator((x, y) -> x * y, 1);
        ExecutorService threadPool = Executors.newFixedThreadPool(8);

        IntStream.range(1, 10).forEach(i -> threadPool.submit(() -> accumulator.accumulate(i)));

        threadPool.shutdown();
        while (!threadPool.isTerminated()) ;
        System.out.println(accumulator.getThenReset());
    }
}
