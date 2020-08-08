### AQS

锁和协作类底层都用了一个共同的基类，即AQS。

典型例子Semaphore、CountDownLatch、ReentrantLock内部都有一个**Sync类**，**Sync类继承了AQS。**



#### AQS类的工作

AQS是一个用于**构建锁、同步器、协作工具类的工具**（框架）。

内容包括：

1. 同步状态的原子性管理
2. 线程的阻塞和解除阻塞
3. 队列的管理



#### AQS核心

- **state**状态
  - volatile修饰，依赖juc.atomic包通过CAS保证getState和setState的线程安全
  - 实现类中可代表不同含义
    - ReentrantLock中：“锁”的占有情况，包括可重入计数
    - Semaphore：剩余的许可证数量
    - CountDownLatch：还需要倒数的数量

- 控制线程抢锁和线程的**FIFO队列**

  - 用来存放"等待的线程"，AQS就是“排队管理器”。
  - AQS会维护一个等待的线程队列，将线程都放入这个队列里。（双向链表）

- **期望协作工具类去实现**的**获取/释放**等重要方法。

  - 获取方法
    - 依赖state变量，经常会阻塞（如*Semaphore::acquire*)
  - 释放方法
    - 释放操作不会阻塞
    - *Semaphore::release*
    - *CountDownLatch::countDown*

  



#### AQS用法

1. 写一个类，想好**协作的逻辑**，实现**获取、释放**方法

2. 内部写一个Sync类继承AbstractQueuedSynchronizer

3. 根据是否独占来重写方法，在之前写的获取、释放方法中调用这些方法

   ```java
   tryAcquire();
   tryRelease();
   tryAcquireShared(int acquires);
   tryReleaseShared(int releases);
   ```

    

#### 源码分析[TODO]

##### Semaphore 

- state表示许可证的剩余数量
- tryAcquire方法
  - 判断：nonfairTryAcquireShared大于等于0的话，代表成功
  - 先检查剩余许可证数量够不够这次需要的，用减法计算
  - 若不够，就返回负数，表示失败
  - 若够了，自旋加CAS来改变state状态
    - 直到改变成功，返回正数
    - 期间被其他线程修改导致剩余数量不够，则返回负数表示失败
- tryRelease方法
  - 由于是可重入的，state代表重入

##### ReentrantLock

- state表示重入次数
- tryRelease方法
  - 是不是当前持有锁的线程释放的。是的话，重入数减一
  - 如果减到了0，说明完全释放了，于是free就是true，并把state设置为0
- tryAcquire方法
  - 类似，判断state和持有线程

