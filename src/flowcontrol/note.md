### 控制并发流程

等待某个事件/线程运行完毕，达到要求。

#### CountDownLatch

倒数门栓

- 主要方法

```java
CountDownLatch(int count) // 构造函数
await()//调用await()方法的线程会被挂起,它会等待直到count值为0才继续执行
countDown() // 将count值减1, 直到为0时,等待的线程会被唤起
```
- 两个典型用法
  - 一等多
  - 多等一



#### CyclicBarrier

和CountDownLatch相似，都能阻塞一组线程

|          | CountDownLatch          | CyclicBarrier |
| -------- | ----------------------- | ------------- |
| 针对对象 | 事件（countdown）       | 线程（await） |
| 可重用性 | 倒数到0后就不可以重用了 | 可重复使用    |

方法

```java
// 每有parties个线程调用了cyclicBarrier.await()后,调用r方法并释放这些线程的阻塞
CyclicBarrier cyclicBarrier = new CyclicBarrier(int parties, Runnable r);
cyclicBarrier.await();
```





#### Semaphore

"许可证"，限制或管理优先的资源

控制临界区最多同时有N个线程访问

```java
new Semaphore(int permits, boolean fair) // 设置是否要使用公平策略,若true,FIFO等待序列
acquire(int permits) //获得x张许可证;默认为1
acquireUninterruptibly(int permits)
release() //需要主动归还许可证
```

 也可以作为轻量级的*CountDownLatch*

```java
//线程1需要线程2完成准备工作后才能开始工作
t1:	semaphore.acquire();
t2: semaphore.relase(); // 获取和释放不一定是同一个线程
```



#### Condition

特点：一个lock锁可以生成多个condition对象

```java
// Lock代替synchronized
// Condition代替相对应的Object.wait/notify，用法和性质几乎一样

Condition condition = lock.newCondition();
condition.await(); //会自动释放锁(所以必须先持有锁)，和Object.wait一样
condtion.signal();
```

