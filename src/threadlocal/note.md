## ThreadLocal

1. 两个作用
   1. 让某个需要用到的对象在线程间隔离（每个线程都有自己的独立对象）
   2. 让任何方法中都可以轻松地获取到该对象

2. 两种场景

   1. initialValue: ThreadLocal第一次get的时候将对象初始化出来：初始化时机由我们控制；**线程不安全的工具类**（SimpleDateFormat）
   2. set: 对象生成时机不由我们控制（User对象）

3. 使用ThreadLocal的好处

   1. 达到线程安全
   2. 不需要加锁，提高执行效率
   3. 高效利用内存，节省开销
   4. 免去传参的繁琐。**低耦合**


4. 内存泄漏问题
   1. ThreadLocal中的Entry(**key是弱引用**,value是强引用)
   2. 若ThreadLocal不被使用了, key被GC回收, value不会; 调用rehash, remove等方法时会主动释放掉key=null的value
   3. 最佳实践(阿里): 主动调用remove