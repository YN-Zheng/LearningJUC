package immutable;

/**
 * 不可变的对象
 * 演示其他类无法修改这个对象,public也不行
 * 不可变的对象一定是线程安全的,不需要采取额外的措施
 * <p>
 * 以前: final修饰方法, 变为内嵌调用, 提高性能
 * 现在:
 * <ol>
 * <li>修饰类: 防止类被继承</li>
 * <li>修饰方法:防止方法被重写</li>
 * <li>修饰变量:防止变量被修改. 如果变量是对象,对象的引用不能变,但自身的内容依然可以变化</li>
 * <li>天生是线程安全的,不需要额外的同步开销</li>
 * <li>不再需要final去提高性能:JVM变聪明了</li>
 * </ol>
 */
public class Person {

    final int age = 18;
    final String name = "Alice";
    String bag = "computer";
}
