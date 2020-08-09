package imooccache.computable;

import java.io.IOException;

/**
 * 耗时计算的实现类,有概率计算失败
 */
public class MayFail implements Computable<String, Integer> {
    @Override
    public Integer compute(String arg) throws Exception {
        double random = Math.random();
        Thread.sleep(3000);
        if (random > 0.5) {
            throw new IOException("读取文件出错");
        }
        return Integer.valueOf(arg);
    }
}
