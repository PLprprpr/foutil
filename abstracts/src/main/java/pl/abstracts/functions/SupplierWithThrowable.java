package pl.abstracts.functions;

import java.util.function.Supplier;

/**
 * 支持带<code>Throwable</code>的<code>Supplier</code>
 *
 * <hr><pre>
 * 假设有如下方法，抛出一个检查异常
 * public static String build() throw Exception {
 *     if (x) {
 *         throw new Exception("CPU太烫了，罢工了");
 *     }
 *     return "";
 * }
 * 或其lambda形式
 * () -> {
 *     if (x) {
 *         throw new Exception("CPU太烫了，罢工了");
 *     }
 *     return "";
 * }</pre>
 * <hr><pre>
 * 如果使用<code>Supplier</code>：
 * public static void deal(Supplier&lt;String&gt; fun) {
 *     fun.get();
 * }
 * deal(s -> build()); // 由于<code>build</code>方法签名和<code>Supplier</code>的签名不符，会报错</pre>
 * <hr>
 * 而使用<code>SupplierWithThrowable</code>可以兼容抛出以及不抛出的情况
 *
 * @author LiYan
 * @see Supplier
 */
@FunctionalInterface
public interface SupplierWithThrowable<T, EX extends Throwable> {

    /**
     * get
     * @see Supplier#get()
     */
    T get() throws EX;


    /**
     * 转换为<code>Supplier</code>，转换时会自动捕获并忽略异常（异常时返回null）
     * @return 去除了异常的<code>Supplier</code>
     */
    default Supplier<T> toSupplier() {
        return () -> {
            try {
                return get();
            } catch (Throwable ignored) {}
            return null;
        };
    }
}
