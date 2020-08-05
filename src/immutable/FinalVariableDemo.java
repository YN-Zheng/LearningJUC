package immutable;

/**
 * 演示final变量
 **/
public class FinalVariableDemo {
    // 静态final
    private static final int b1 = 0;
    private static final int b2;

    static {
        b2 = 0;
    }

    // 类中的final属性(三种)
    // 1. 声明变量时赋值
    // 2. 构造函数中赋值
    // 3. 类的初始代码块中赋值(不常用)
    // private final int a;
    private final int a1 = 6;
    private final int a2;
    private final int a3;

    {
        a2 = 0;
    }

    public FinalVariableDemo(int a3) {
        this.a3 = a3;
    }

    // 方法中:不规定赋值时机,只要求在使用前必须赋值
    // 这和方法中的非final变量的要求也是一样的
    void testFinal() {
        final int c1;
        c1 = 0;
        int temp = c1;
    }
}

