package imooccache.computable;

/**
 * 有一个计算函数computer,用来代表耗时计算
 * 每个计算器都要事先这个接口,无侵入实现缓存功能
 */
public interface Computable<A, V> {
    V compute(A arg) throws Exception;
}
