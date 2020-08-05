package immutable;

import java.util.HashSet;
import java.util.Set;

/**
 * 一个属性是对象,但是整体不可变,其他类无法修改set中的数据
 * 总结:对象不可变的条件
 * 1. 对象创建后, 其状态不能被修改
 * 2. 所有属性都是final修饰的
 * 3. 对象创建过程中没有发生逸出
 */
public class ImmutableDemo {
    private final Set<String> students = new HashSet<>();

    public ImmutableDemo() {
        students.add("小米");
        students.add("王壮");
    }

    public boolean isStudent(String name) {
        return students.contains(name);
    }
}
