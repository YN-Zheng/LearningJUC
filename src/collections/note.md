## 并发集合

concurrent包提供的容器分为三类:
1. Concurrent*:大部分通过CAS实现并发
2. CopyOnWrite*:通过复制一份原数据
3. Blocking*:通过Reentrantlock(AQS)实现的



### ConcurrentHashMap

ConcurrentHashMap 和 HashMap一致的地方

|                      |      |                                                      |
| -------------------- | ---- | ---------------------------------------------------- |
| TREEIFY_THRESHOLD    | 8    | 域值: 链表->树                                       |
| UNTREEIFY_THRESHOLD  | 6    | 域值: 树->链表                                       |
| DEFAULT_CAPACITY     | 16   | 初始容量. 2的次幂(加快扰动函数的速度: **h^(h>>>16**) |
| MIN_TREEIFY_CAPACITY | 64   | 若需要树化且容量低于这个域值,则改为增加: 容量x2      |

#### null值key?

HashMap中, null值对应的hashcode为0, 而ConcurrentHashMap以及HashTable中会抛出NullPointerException异常。

ConcurrentHashMap使用的是fail-safe机制，使得此次读到的数据不一定是最新的数据。

使用key为null的数据，会导致无法判断key不存在或是key存在但值为空。并发情况下也无法再一次调用contains(key)来进行判断。

##### fail-safe

JUC包下的容器都是fail-safe。可以在多线程下并发使用，只是此次读到的数据不一定是最新的数据。

##### fail-fast

java集合中的一种机制， 在用迭代器遍历一个集合对象时，如果遍历过程中对集合对象的内容进行了修改（增加、删除、修改），则会抛出Concurrent Modification Exception。

原理：modCount。每当迭代器遍历下一个元素前，都会检测modCount是否变化。

#### 

#### 实现：JDK 7前

分段锁技术，Segment继承于ReentrantLock

数组＋链表





#### 实现：JDK 8后

- CAS + synchronized来保证
  - CAS
  - synchronized
- 

- 和HashMap类似

- volatile修饰Node的值和next

- 链表 - - - 》 树的转换

```java
if(key == null) throw new NullPointerException();
for(){
	if(tab == null) tab = initTable()
	else if(hash对应的槽中无节点){
		if(CAS赋值成功){
			break;
		}
	}
	else if(其他线程resize){
		...
	}
	else{
		synchronized(hash对应的槽中的第一个节点){
			...
		}
	}
}
```



### CopyOnWriteArrayList

- 代替Vector和SynchronizedList
- 读多写少
- 实现原理
  - 创建新副本、读写分离
  - “不可变原理”：旧容器不可变
- 读写规则
  - 读不需要加锁
  - 写入和写入之间需要进行同步等待
- 缺点
  - 内存占用问题：复制机制，同时两个对象的内存
  - 数据一致性：最终一致，不能保证实时一直



### 阻塞队列

* 阻塞队列:具有阻塞功能的队列
* 阻塞功能:
  * take()获取并移除队列的头结点;若此时队列里无数据,则阻塞,知道队列里有数据
  * put()插入元素.但是如果队列已满,那么就无法继续插入,则阻塞,直到队列里有了空闲空间
* 阻塞队列和线程池的关系:重要的组成部分

```
* :   有界;指定容量;公平:指定公平的话,等待最长时间的线程会优先处理
* : 	无界;容量Integer.MAX_VALUE;内部结果:Node,两把锁.分析put方法
```

|          | ArrayBlockingQueue                             | LinkedBlockingQueue                 |
| -------- | ---------------------------------------------- | ----------------------------------- |
|          | 有界                                           | 无界                                |
|          | 指定容量                                       | Integer.MAX_VALUE                   |
|          | 公平（指定公平的话，等待最长的线程会优先处理） |                                     |
| 内部结构 |                                                | 链表、两把锁（take和put互不干扰）、 |
|          |                                                |                                     |

