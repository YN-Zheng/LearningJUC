package immutable;

/**
 * final的方法
 */
public class FinalMethodDemo {
    // 不能修饰static方法
    public static void sleep() {

    }

    // 不能修饰构造方法
    public void drink() {

    }

    // 不能重写final方法
    public final void eat() {

    }

}

class SubClass extends FinalMethodDemo {
    //看似一样,实际上是不同类的静态方法
    public static void sleep() {

    }

    @Override
    public void drink() {
        super.drink();
    }
}
