package threadlocal;

/**
 * 演示ThreadLocal用法2:避免传递参数的麻烦
 * 总结:
 * 1. ThreadLocal的两个作用
 *    1. 让某个需要用到的对象在线程间隔离(每个线程都有自己的独立的对象)
 *    2. 在任何方法中都可以轻松的获取到该对象
 * 2. 两种场景：根据共享对象的生成时机不同
 *    1. 场景一：initialValue   ThreadLocal第一次get的时候就把对象初始化出来；对象的初始化时机由我们控制；线程不安全的工具类
 *    2. 场景二：set            对象生成时机不由我们控制
 * 3. 使用ThreadLocal的好处
 *    1. 达到线程安全
 *    2. 不需要加锁， 提高执行效率
 *    3. 高效地利用内存，节省开销
 *    4. 免去传参的繁琐。低耦合。
 * 4. 内存泄漏问题
 *    1. ThreadLocal中的Entry(key是弱引用,value是强引用)
 *    2. 若ThreadLocal不被使用了, key被GC回收, value不会; 调用rehash, remove等方法时会主动释放掉key=null的value
 *    3. 最佳实践(阿里): 主动调用remove
 * 5.
 *
 */
public class ThreadLocalNormalUsage06 {
    public static void main(String[] args) {
        new Service1().process();
    }
}

class Service1{
    public void process(){
        User user = new User("超哥");
        UserContextHolder.holder.set(user);
        new Service2().process();
        new Service3().process();

    }
}

class Service2{
    public void process(){
        User user = UserContextHolder.holder.get();
        System.out.println("Service2拿到用户"+user.getName());
        UserContextHolder.holder.remove();
        System.out.println("Service2删除用户");
    }
}

class Service3{
    public void process(){
        User user = UserContextHolder.holder.get();
        System.out.println("Service3拿到用户"+user.getName());
    }
}

class UserContextHolder{
    public static ThreadLocal<User> holder = new ThreadLocal<>();
}

class User{
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}