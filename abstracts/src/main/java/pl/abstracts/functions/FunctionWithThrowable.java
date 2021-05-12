package pl.abstracts.functions;

import java.util.function.Function;

/**
 * 支持带<code>Throwable</code>的<code>Function</code>
 *
 * <hr><pre>
 * 假设有如下方法，抛出一个检查异常
 * public static String fun(String s) throw Exception {
 *     if (s == null) {
 *         throw new Exception("NULL");
 *     }
 *     return "OK";
 * }
 * 或其lambda形式
 * s -> {
 *     if (s == null) {
 *         throw new Exception("NULL");
 *     }
 *     return "OK";
 * }</pre>
 * <hr><pre>
 * 如果使用Function：
 * public static void deal(Function&lt;String, String&gt; fun) {
 *     fun.apply("");
 * }
 * deal(s -> fun(s)); // 由于<code>fun</code>方法签名和<code>Function</code>的签名不符，会报错</pre>
 * <hr>
 * 而使用<code>FunctionWithThrowable</code>可以兼容抛出以及不抛出的情况
 *
 * @author LiYan
 * @see Function
 */
@FunctionalInterface
public interface FunctionWithThrowable<T, R, EX extends Throwable> {

    /**
     * apply
     * @see Function#apply(T)
     */
    R apply(T t) throws EX;

    /**
     * 转换为<code>Function</code>，转换时会自动捕获并忽略异常（异常时返回null）
     * @return 去除了异常的<code>Function</code>
     */
    default Function<T, R> toFunction() {
        return t -> {
            try {
                return apply(t);
            } catch (Throwable ignored) {}
            return null;
        };
    }

}
