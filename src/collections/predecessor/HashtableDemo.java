package collections.predecessor;

import java.util.Hashtable;

/**
 * 早起JDK中的线程安全的集合
 * 性能差:synchronized
 */
public class HashtableDemo {
    public static void main(String[] args) {
        Hashtable<String, String> table = new Hashtable<>();
        table.put("money", "10000");
        System.out.println(table.get("money"));
    }
}
