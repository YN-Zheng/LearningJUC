package threadpool;

/**
 * 手动设置
 * 线程池里的线程数量设置为多少比较合适？
 * - CPU密集型：CPU核心数的1-2倍左右
 * - 耗时IO型：以JVM线程监控显示繁忙情况为根据
*  - 参考公式：线程数 = CPU核心数*(1+平均等待时间/平均工作时间）
 */
public class DIYThreadPool {
}
