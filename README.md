# JUC网课学习笔记

线程池+各种锁+CAS+AQS+ThreadLocal+ConcurrentHashMap+并发综合实战项目



## 线程池 (CH3-CH4)

### 使用线程池

线程池减少了新建和销毁线程带来的开销。

Executors 中提供了设定好参数的线程池：

```java
// 线程数设置为1,使用LinkedBlockingQueue; 请求堆积,大量内存
ExecutorService newSingleThreadExecutor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

// 自定义线程数;队列没有容量上限,内存溢出OOM
ExecutorService newFixedThreadPool = new ThreadPoolExecutor(threadDIY, threadDIY,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

// 可能导致创建非常多的线程,导致OOM
ExecutorService newCachedThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

```

我们也可以自己DIY一个，需要指定定的参数有：

```java
    /**
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *        pool
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     * @param unit the time unit for the {@code keepAliveTime} argument
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue)
```

### 关闭线程池的相关方法

```java

pool.shutdown();// 中断线程,拒绝新任务
pool.shutdownNow();// 中断进行中的线程, 停止等待中的线程,并返回等待中的线程
pool.isShutdown();// 是否进入停止状态（不一定完全结束）
pool.isTerminated(); // 是否完全停止
boolean terminated = pool.awaitTermination(20L,TimeUnits.SECONDS);//等待,直到线程池中所有的线程都被停止了
```



### ThreadLocal

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



## 锁(CH5)

### 锁的分类

| 分类依据                     |                    |                      |
| :--------------------------- | ------------------ | -------------------- |
| 是否锁住同步资源             | 悲观锁（互斥同步） | 乐观锁（非互斥同步） |
| 能否共享同一把锁             | 共享锁             | 独占锁               |
| 多线程竞争时，是否排队       | 排队：公平锁       | 先尝试插队：非公平锁 |
| 同一线程能否重复获取同一把锁 | 可重入锁           | 不可重入锁           |
| 是否可中断                   | 可中断锁           | 非可中断锁           |
| 等锁的过程                   | 自旋锁             | 阻塞锁               |

#### 乐观

- 实现：CAS算法

- 适用场景
  - **临界区小**，持锁时间短（时间越长，相比悲观锁的开销就越大）
  - **多读少写**

- 典型例子
  - 原子类
  - 并发容器
  - synchronized锁优化后的（轻量级锁
  - GIT
  - 数据库（version控制数据库？？？TODO）

#### 可重入锁 ReentrantLock

相比synchronized关键字，有一些高级功能

1. 等待可中断
2. 非公平锁、公平锁（和synchronized一样，默认都是非公平的）
3. 可绑定多个条件（和锁对象的wait()等类似）



源码分析

- 获取锁时先判断。若当前线程就是已经占有锁的线程，则status+1
- 释放锁时先判断是否当前线程持有锁，然后判断status。若status=0，才真正释放锁。



#### 非公平锁

- 插队以提高效率：吞吐量更大；**线程饥饿**问题

- 避免唤醒带来的**空档期**

- **tryLock**不遵守公平，自带插队属性



#### 共享锁和排他锁

- 解决痛点：多读场景没有线程安全问题

- 排他锁：独占锁、独享锁
- 共享锁：又称为读锁。可以查看但无法修改和删除数据

- 插队？
  - 公平锁：不允许插队
  - 非公平锁
    - 写锁可以随时插队 
    - 读锁仅在：等待队列头节点不是想获取写锁的时候，可以插队

```java
// JUC 中的读写锁
ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();


// 升降级策略：只能降级，不能升级
writeLock.lock();
try {
    readLock.lock();  //"在不释放写锁的情况下，直接获取读锁，成功降级");
} catch (InterruptedException e) {
    e.printStackTrace();
} finally {
    readLock.unlock();
    writeLock.unlock();
}
```







### synchronized关键字



### Lock接口

特点

- 全新的互斥同步手段。
  - 非块结构实现互斥同步，类库层面实现同步
  - 不同调度算法、不同特征、不同性能、不同语意的锁成为可能

- 不会像synchronized一样异常时释放锁（最佳实践：finally中释放锁）

- **可见性保证**：和synchronized有同样的内存语意。下一个线程加锁后可以看见所有前一个线程解锁前发生的所有操作

方法

```java
lock();
unlock(); //解锁
tryLock() //尝试获取锁, 若获取失败则立即返回
tryLock(long time, TimeUnit unit) //尝试获取锁, 一段时间后超时就放弃
lockInterruptibly() // 相当于无限时间的tryLock(long time, TimeUnit unit). 等待过程中,可以被中断
```

### synchronized

#### 特点

1. 试图获得锁不能设定超时
2. 不能中断一个正在试图获得锁的线程
3. 不够灵活（相比：读写锁），加锁和释放锁的时机单一

#### 原理

1. synchronized关键字经过Javac编译之后，会在同步块的前后分别形成**monitorenter**和**monitorexit**这两个字节码指令。
2. 这两个字节码指令都需要一个reference类型的参数来指明要**锁定和解锁的对象**。
3. 若没有明确指定，则根据synchronized修饰的方法类型（实例方法或类方法）来决定是取**代码**所在的**对象实例**还是取类型对应的**Class对象**来作为线程要持有的锁。

从执行成本的角度看，持有锁是一个**重量级操作**。主流的Java虚拟机实现中，Java的**线程**是**映射**到操作系统的**原生内核线程**之上的。阻塞或唤醒一条线程需要操作系统来帮忙完成，这就不可避免地陷入**用户态到核心态的转换**中，这种状态转换需要消耗很多的处理器时间。

所以JDK 6中加入了大量针对synchronized锁的优化措施，与ReentrantLock的性能基本持平。

### JVM对锁的优化 





#### synchronized锁优化

##### 对象头内存布局

HotSpot虚拟机的对象头（Object Header）分为两部分；

1. Mark Word：存储对象自身的运行时数据，如哈希吗、GC分代年龄等。
2. 存储指向**方法区对象类型数据**的指针



Mark Word不同状态下的存储内容如图所示

| 锁状态 | 30bit                                        | 2bit(标志位) |
| ------ | -------------------------------------------- | ------------ |
| 未锁定 | 对象哈希码(25bit)+分代年龄(4bit)+偏向模式(0) | 01           |
| 可偏向 | 线程ID(23)+EPOCH(2bit)+分代年龄+偏向模式(1)  | 01           |
| 轻量级 | 指向调用栈中锁记录的指针                     | 00           |
| 重量级 | 指向重量级锁的指针                           | 10           |
| GC标记 | 空                                           | 11           |



##### 轻量锁

经验法则：大部分的锁，整个同步期间都是**不存在竞争**的。

若没有竞争，轻量级锁便通过CAS操作成功避免了使用互斥量的开销。

|        | 原理                           | 场景                   |
| ------ | ------------------------------ | ---------------------- |
| 轻量锁 | CAS                            | 没有多线程竞争的前提下 |
| 重量锁 | 使用操作系统互斥量，性能损耗大 |                        |

轻量锁的工作过程：

1. 进入同步块之前，如果此同步对象没有被锁定，虚拟机首先在当前线程的栈帧中建立一个名为锁记录的空间（Lock Record），用于存储锁对象目前的Mark Word的拷贝。
2. 使用CAS操作尝试把对象的Mark Word更新为指向Lock Record的指针。
   1. 若成功，标志位变为00，代表该线程拥有了这个对象的锁，并处于轻量级锁定状态。
   2. 若失败，首先检查对象的Mark Word是否指向当前线程的栈帧。若当前线程已经拥有了这个对象的锁，那么直接进入同步块继续执行就行。
   3. 否则说明这个锁对象已经被其他线程抢占。必须要升级为重量级锁，锁标志变为“10”，此时Mark Word中存储的就是指向重量级锁（互斥量）的指针，后面等待锁的线程也必须进入阻塞状态。
3. 解锁时同样也是通过CAS操作完成的



##### 偏向锁

目的：**消除**数据在无竞争情况下的同步，连轻量锁的CAS操作都不需要了。

实现：锁会偏向于第一个获得它的线程。如果接下来的执行过程中，该锁一直没有被其他线程获取，则持有偏向锁的线程将永远不需要同步。

1. 锁对象第一次被线程获取时，标志设为01，偏向模式设为1，同时使用CAS操作把线程ID记录在对象的Mark Word中。
2. 一旦出现另一个线程去尝试获取这个锁的情况，偏向模式马上宣告结束，标志位恢复到“未锁定”或“轻量级锁定”的状态。



特点：占用对象哈希码空间来存储线程ID了。

1. 一个对象已经计算过一次一致性哈希码后，再也**无法进入偏向锁状态**了
2. 一个对象当前正处于偏向锁状态，有收到需要计算其一致性哈希码请求时，它的偏向状态会被立即撤销，并且锁会膨胀为**重量级锁**







#### 锁消除

虚拟机计时编译器在运行时，对一些代码要求同步，但是对被检测到不可能存在共享数据竞争的锁进行消除。

变量是否逃逸，程序员自己是清楚的，怎么会在明知道不存在数据征用的情况下还要求同步呢？事实上，有许多同步措施不是程序猿自己加入的，同步的代码在java中出现的频繁程度其实很高。

```java
public String concatString(String s1, String s2, String s3){
    return s1 + s2 + s3;
}

//javac转化后的字符串连接操作
//JDK 5之前,会转化为StringBuffer对象的连续append()操作
public String concatString(String s1,String s2, String s3){
    StringBuffer sb = new StringBuffer();
    sb.append(s1);
    sb.append(s2);
    sb.append(s3);
    return sb.toString();
}
/* 由于StringBuffer线程安全，每个方法中都有一个同步块。虚拟机观察变量sb，经过逃逸分析后发现它的动态作用域被限制在concatString（）方法内部，故sb的所有引用不会逃逸到方法之外，这里的锁可以被安全地消除掉
*/

// !!!JDK 5后, 实际上会转为非线程安全的StringBuilder来完成字符串拼接
```



#### 锁粗化（锁膨胀）

原则上，同步块的作用范围应该尽可能的小，使得待锁的线程能尽快地拿到锁。大多数情况下这个原则是正确的。

但若一系列的连续操作都是对同一个对象反复加锁和解锁，甚至加锁操作是出现在循环体之中的，那即使没有线程竞争，频繁地进行互斥同步操作也会导致不必要的性能损耗。

如果虚拟机探测到有这样一串零碎的操作都对同一个对象加锁，将会把加锁同步的范围扩展（粗化）到整个操作序列的外部。





### 优化策略

1. 缩小同步代码块
2. 尽量不要锁住方法
3. 减少锁的次数
4. 避免人为制造“热点”
5. 锁中尽量不要再包含锁
6. 选择合适的类型或合适的工具类



## 对象不可变(CH8)

**不可变**的对象一定是**线程安全**的，无论是对象的方法实践还是方法的调用者，都不需要再进行任何线程安全保障措施。

```java
public class Integer{
	private final int value;
}
```



### 线程安全

当多个线程访问同一个对象时，如果不用考虑这些线程在运行时环境下的调度和交替执行，也不需要进行额外的同步，或者在调用方进行额外的协调操作，调用这个对象的行为都可以获得正确的结果，那就称这个对象是线程安全的。

## [CH10：控制并发流程](src/flowcontrol/note.md)

## [CH11：AQS](src/aqs/note.md)



## 总结

### 并发工具类分类

1. 为了并发安全：互斥同步，费互斥同步，无同步方案
2. 管理线程，提高效率
3. 线程协作


