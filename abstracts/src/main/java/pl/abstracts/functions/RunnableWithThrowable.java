package pl.abstracts.functions;

import java.util.function.Consumer;

/**
 * 支持带<code>Throwable</code>的<code>Runnable</code>
 *
 * <hr><pre>
 * 假设有如下方法，抛出一个检查异常
 * public static void run() throw Exception {
 *     throw new Exception("CPU太烫了，罢工了");
 * }
 * 或其lambda形式
 * () -> throw new Exception("CPU太烫了，罢工了");</pre>
 * <hr><pre>
 * 如果使用<code>Runnable</code>：
 * public static void deal(Runnable fun) {
 *     fun.run();
 * }
 * deal(s -> run()); // 由于<code>run</code>方法签名和<code>Runnable</code>的签名不符，会报错</pre>
 * <hr>
 * 而使用<code>ConsumerWithThrowable</code>可以兼容抛出以及不抛出的情况
 *
 * @author LiYan
 * @see Consumer
 */
@FunctionalInterface
public interface RunnableWithThrowable<EX extends Throwable> {

    /**
     * run
     * @see Runnable#run()
     */
    void run() throws EX;

    /**
     * 转换为<code>Function</code>，转换时会自动捕获并忽略异常
     * @return 去除了异常的<code>Function</code>
     */
    default Runnable toRunnable() {
        return () -> {
            try {
                run();
            } catch (Throwable ignored) {}
        };
    }

}
