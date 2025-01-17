package imooccache;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 最简单的缓存形式:HashMap
 * 问题:多线程时并发不安全
 */
public class ImoocCache1 {
    private final HashMap<String, Integer> cache = new HashMap<>();


    public static void main(String[] args) throws InterruptedException {
        ImoocCache1 imoocCache1 = new ImoocCache1();
        System.out.println("开始计算了");
        Integer result = imoocCache1.computer("13");
        System.out.println("第一次计算结果: " + result);
        result = imoocCache1.computer("13");
        System.out.println("第二次计算结果: " + result);
    }

    public synchronized Integer computer(String userId) throws InterruptedException {
        Integer result = cache.get(userId);
        //先检查HashMap里面有没有保存过之前的计算结果
        if (result == null) {
            //如果缓存中找不到,那么需要计算一下结果,并且保存到hashmap中
            result = doCompute(userId);
            cache.put(userId, result);
        }
        return result;
    }

    private Integer doCompute(String userId) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        return Integer.valueOf(userId);
    }
}
