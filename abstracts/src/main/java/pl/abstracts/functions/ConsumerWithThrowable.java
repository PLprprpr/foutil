package pl.abstracts.functions;

import java.util.function.Consumer;

/**
 * 支持带<code>Throwable</code>的<code>Consumer</code>
 *
 * <hr><pre>
 * 假设有如下方法，抛出一个检查异常
 * public static void fun(Integer i) throw Exception {
 *     if (i == null) {
 *         throw new Exception("NULL");
 *     }
 * }
 * 或其lambda形式
 * i -> {
 *     if (i == null) {
 *         throw new Exception("NULL");
 *     }
 * }</pre>
 * <hr><pre>
 * 如果使用Consumer：
 * public static void deal(Consumer&lt;Integer&gt; fun) {
 *     fun.accept(5);
 * }
 * deal(i -> fun(i)); // 由于<code>fun</code>方法签名和<code>Consumer</code>的签名不符，会报错</pre>
 * <hr><pre>
 * 而使用<code>ConsumerWithThrowable</code>可以兼容抛出以及不抛出的情况
 * 比如：deal(Thead::sleep); // 不会真的有人能忍受InterruptException吧？
 * </pre>
 *
 * @author LiYan
 * @see Consumer
 */
@FunctionalInterface
public interface ConsumerWithThrowable<T, EX extends Throwable> {

    /**
     * accept
     * @see Consumer#accept(T)
     */
    void accept(T t) throws EX;

    /**
     * 转换为<code>Consumer</code>，转换时会自动捕获并忽略异常
     * @return 去除了异常的<code>Consumer</code>
     */
    default Consumer<T> toConsumer() {
        return t -> {
            try {
                accept(t);
            } catch (Throwable ignored) {}
        };
    }
}

