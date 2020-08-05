package immutable;

/**
 * 和1的区别:方法拿到,运行时确定,编译器不会进行优化
 */
public class InterviewDemo2 {
    public static void main(String[] args) {
        String a = "zyn2";
        final String b = getZYN();
        String c = b + 2;
        System.out.println(a == c);
    }

    private static String getZYN() {
        return "zyn";
    }
}
