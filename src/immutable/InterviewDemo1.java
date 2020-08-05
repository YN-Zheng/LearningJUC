package immutable;

/**
 * final引起的编译优化
 */
public class InterviewDemo1 {
    public static void main(String[] args) {
        String a = "zyn2";
        String b = "zyn";
        //被final修饰,故编译时就会自动优化, c不需要新建对象, 直接将用到c的变量(此处为e)指向常量池中已经有的"zyn2"
        final String c = "zyn";
        //运行时确定,会在堆上生成字符串对象"zyn2"
        String d = b + 2;
        String e = c + 2;
        System.out.println(b == c); //true
        System.out.println(a == d); // false
        System.out.println(a == e); // true
    }
}
