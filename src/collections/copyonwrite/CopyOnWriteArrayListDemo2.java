package collections.copyonwrite;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 迭代时数据过去问题
 * CopyOnWriteArrayList缺点总结:
 * 数据一致性问题: 只能保证数据的最终一致性,不能保证数据的实时一致性(写入的数据不一定能立马被读到)
 * 内存占用问题:复制机制,在进行写操作的时候,内存里会同时驻扎两个对象的内存
 */
public class CopyOnWriteArrayListDemo2 {
    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(new Integer[] {1, 2, 3});
        System.out.println(list);
        Iterator<Integer> iterator1 = list.iterator();
        list.add(4);
        System.out.println(list);
        Iterator<Integer> iterator2 = list.iterator();

        iterator1.forEachRemaining(System.out::println);
        iterator2.forEachRemaining(System.out::println);

    }
}
