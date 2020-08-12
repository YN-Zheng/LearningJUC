package collections.concurrenthashmap;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * putVal流程
 * 1.判断key value不为空
 * 2. 计算hash值
 * 3. 根据对应位置节点的类型, 来赋值,或者helpTransfer,或者增长链表,或者给红黑树增加节点
 * 4. 满足域值就红黑树话
 * 5. 返回oldVal
 * get流程
 * 1. 计算hash值
 * 2. 找到对应位置,根据情况进行: 直接取值;红黑树里找值;遍历链表取值;
 *
 * 为什么超过8要转为红黑树?
 * 1. 默认为链表. 每个红黑树节点大小是链表的节点的两倍
 * 2. 超过8的概率很小
 */
public class ConcurrentHashMapDemo {

    public static void main(String[] args) {
        Hashtable<String, Integer> table = new Hashtable<>();
        HashMap<String, Integer> map2 = new HashMap<>();
        map2.put(null, 2);
        System.out.println(map2.get(null));
    }
}
