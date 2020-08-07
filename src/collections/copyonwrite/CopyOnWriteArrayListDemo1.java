package collections.copyonwrite;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 演示CopyOnWriteArrayList可以在迭代的过程中修改数组内容,但是ArrayList不行
 * 实现原理
 * 1. CopyOnWrite含义:拷贝,修改,再把数组指针指向新的内存
 * 2. 读写分离
 * 3. "不可变"原理:旧的容器不可变, 线程安全
 * 4. 迭代的时候
 */
public class CopyOnWriteArrayListDemo1 {
    public static void main(String[] args) {
//        ArrayList<String> list= new ArrayList<>();
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println("list is" + list);
            String next = iterator.next();
            System.out.println(next);
            if (next.equals("2")) {
                list.remove("5"); //java.util.ConcurrentModificationException
            }
            if (next.equals("3")) {
                list.add("3 found");
            }
        }
    }
}
