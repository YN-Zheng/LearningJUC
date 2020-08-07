package collections.predecessor;

import java.util.Vector;

/**
 * Vector源码:synchronized修饰
 */
public class VectorDemo {
    public static void main(String[] args) {
        Vector<String> vector = new Vector<>();
        vector.add("test");
        System.out.println(vector.get(0));

    }
}
