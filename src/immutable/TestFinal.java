package immutable;

/**
 * 测试final能否被修改
 */

public class TestFinal {
    public static void main(String[] args) {
        final Person person = new Person();
//        person.age = 19; // 会报错
//        person = new Person(); // 修饰对象时,不能修改变量引用
//        person.bag="suitcase";// 但是对象本身是可以变化的
        int score = 0;

        //final的赋值时机:属性被声明为final后,该变量只能被赋值一次.且一旦被赋值,final的变量就不能再被改变,无论如何也不会变.
    }
}
