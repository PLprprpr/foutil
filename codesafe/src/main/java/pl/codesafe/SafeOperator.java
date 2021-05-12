package pl.codesafe;

import com.sun.istack.internal.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import pl.abstracts.functions.RunnableWithThrowable;
import pl.abstracts.functions.SupplierWithThrowable;
import pl.codesafe.exception.SafeExceptionHandler;
import pl.codesafe.exception.UnHandledException;
import pl.codesafe.util.CheckUtils;
import pl.codesafe.util.ParseUtils;

/**
 * 一个用于安全处理数据的类，可以忽略处理过程中一些不太重要的异常，改用默认值替代之
 * <p>{不是吧阿sir，我就踩死只蚂蚁，非要报错不让我走路嘛？什么？还要我出门前就想好踩死蚂蚁该怎么办？}</p>
 * <p></p>
 * <p>使用时先静态引入： SaferOperator safer = {@linkplain SaferManager#saferFor(Class) SaferManager.saferFor}(AClass.class);</p>
 * <p></p>
 * <p>之所以采用多个safer实例的方案，是因为这样可以为不同场景设计不同的异常处理器：</p>
 * <p>{@linkplain SaferManager#addSaferExceptionHandlers(Class, SafeExceptionHandler...) SaferManager.addSaferExceptionHandlers}(ShowModelService.class, ex -> {log.warn("safer warn", ex); return null;}) // 因为没有将ex返回，异常处理到此为止，不会抛出</p>
 * <p>SaferManager.addSaferExceptionHandlers(PayService.class, ex -> ex) // 因为处理器中返回了ex，会传递给下一个处理器，如果最终还是返回ex，会抛出ex</p>
 * <p>大致包含以下功能：</p>
 * <hr><pre>
 * 无脑执行系列，懒，就是不想处理异常，执行失败了就失败了吧
 * safer.execute(() -> Thread.sleep(3000)); // 执行时会忽略异常 {不会有人真的想处理InterruptException吧？}</pre>
 * <hr><pre>
 * ensure系列，确保常用类型不为null {全世界的人都知道null.length()和null.size()的结果应该是什么，只有NPE不知道}
 *     Integer value = null;
 *     safer.ensure(value); // 返回0
 *     String str = null;
 *     safer.ensure(str); // 返回""
 *     List list = null;
 *     safer.ensure(list); // 返回new ArrayList()
 *     int totalSize = safer.ensure(str).length() + safer.ensure(list).size();
 * 支持所有基本类型和List、Set、Map等常用类型</pre>
 * <hr><pre>
 * get系列，尝试获取值，如果失败或得到null，取该类型的默认值 {我从A里边取B，我为什么要判断A是否为null啊？}
 *     safer.getList(() -> request.getQueryIds()); // 如果request为null或queryIds为null，返回new ArrayList()
 *     safer.getInt(() -> 1/0); // 返回0
 *     safer.getStr(() -> null); // 返回""
 *     safer.get(() -> JSON.parseObject(json)); // 执行异常时返回null
 * 支持ensure系列的所有类型</pre>
 * <hr><pre>
 * parse系列，自动解析，如果解析失败，返回对应类型的默认值
 *     safer.parseInt("233"); // 返回233
 *     safer.parseLong("100abc"); // 返回0
 *     safer.parseList("[1,2,3}", Integer.class); // 因为jsonArray的结构错误，解析失败，返回new ArrayList&lt;Integer&gt;()
 * 支持常用类型的解析</pre>
 * <hr><pre>
 * stream系列 {safer起手，天下我有}
 *     List&lt;String&gt; list = null;
 *     safer.stream(list); // 返回Stream.empty();
 * 支持Collection和数组类型</pre>
 *
 * @see SaferManager#saferFor(Class)
 * @see SaferManager#newSaferWithThrowableHandlers(Class, List)
 * @see SaferManager#addSaferExceptionHandlers(Class, List)
 * @see SafeExceptionHandler
 * @author LiYan
 */
public class SafeOperator {

    SafeOperator() {
    }

    final List<SafeExceptionHandler> exceptionHandlers = new ArrayList<>();

    public void handleException(Throwable ex) {
        for (SafeExceptionHandler handler : exceptionHandlers) {
            ex = handler.handle(ex);
        }
        if (ex != null) {
            throw new UnHandledException(ex);
        }
    }

    /**
     * 安全执行某个函数，忽略其中所有异常
     * @param runnable 执行函数
     */
    public <EX extends Throwable> void execute(@Nonnull RunnableWithThrowable<EX> runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            handleException(t);
        }
    }

    /**
     * 安全执行某个取值函数，忽略其中所有异常，返回执行得到的值，失败时返回null
     * @param supplier 取值函数
     * @param <T> 执行函数的返回值类型
     * @return 执行函数的返回值，执行失败时返回null
     */
    @Nullable
    public <T, EX extends Throwable> T get(@Nonnull SupplierWithThrowable<T, EX> supplier) {
        try {
            return supplier.get();
        } catch (Throwable t) {
            handleException(t);
        }
        return null;
    }

    /* 常用类型的null ensure */

    public boolean ensure(Boolean value) {
        return value != null ? value : false;
    }

    public String ensure(String value) {
        return value != null ? value : "";
    }

    public byte ensure(Byte value) {
        return value != null ? value : 0;
    }

    public short ensure(Short value) {
        return value != null ? value : 0;
    }

    public int ensure(Integer value) {
        return value != null ? value : 0;
    }

    public long ensure(Long value) {
        return value != null ? value : 0;
    }

    public float ensure(Float value) {
        return value != null ? value : 0;
    }

    public double ensure(Double value) {
        return value != null ? value : 0;
    }

    public BigDecimal ensure(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    public <E> List<E> ensure(List<E> value) {
        return value != null ? value : new ArrayList<>();
    }

    public <E> Set<E> ensure(Set<E> value) {
        return value != null ? value : new HashSet<>();
    }

    public <K, V> Map<K, V> ensure(Map<K, V> value) {
        return value != null ? value : new HashMap<>();
    }

    public <EX extends Throwable> boolean getBool(@Nonnull SupplierWithThrowable<Boolean, EX> supplier) {
        return ensure(get(supplier));
    }

    public <EX extends Throwable> String getStr(@Nonnull SupplierWithThrowable<String, EX> supplier) {
        return ensure(get(supplier));
    }

    public <EX extends Throwable> byte getByte(@Nonnull SupplierWithThrowable<Byte, EX> supplier) {
        return ensure(get(supplier));
    }

    public <EX extends Throwable> short getShort(@Nonnull SupplierWithThrowable<Short, EX> supplier) {
        return ensure(get(supplier));
    }

    public <EX extends Throwable> int getInt(@Nonnull SupplierWithThrowable<Integer, EX> supplier) {
        return ensure(get(supplier));
    }

    public <EX extends Throwable> long getLong(@Nonnull SupplierWithThrowable<Long, EX> supplier) {
        return ensure(get(supplier));
    }

    public <EX extends Throwable> float getFloat(@Nonnull SupplierWithThrowable<Float, EX> supplier) {
        return ensure(get(supplier));
    }

    public <EX extends Throwable> Double getDouble(@Nonnull SupplierWithThrowable<Double, EX> supplier) {
        return ensure(get(supplier));
    }

    public <EX extends Throwable> BigDecimal getBigDecimal(@Nonnull SupplierWithThrowable<BigDecimal, EX> supplier) {
        return ensure(get(supplier));
    }

    public <E, EX extends Throwable> List<E> getList(@Nonnull SupplierWithThrowable<List<E>, EX> supplier) {
        return ensure(get(supplier));
    }

    public <E, EX extends Throwable> Set<E> getSet(@Nonnull SupplierWithThrowable<Set<E>, EX> supplier) {
        return ensure(get(supplier));
    }

    public <K, V, EX extends Throwable> Map<K, V> getMap(@Nonnull SupplierWithThrowable<Map<K, V>, EX> supplier) {
        return ensure(get(supplier));
    }

    public String parseStr(Object o) {
        return ensure(ParseUtils.parseStr(o));
    }

    public boolean parseBool(String str) {
        return ensure(ParseUtils.parseBoolean(str));
    }

    public byte parseByte(String str) {
        return ensure(ParseUtils.parseByte(str));
    }

    public short parseShort(String str) {
        return ensure(ParseUtils.parseShort(str));
    }

    public int parseInt(String str) {
        return ensure(ParseUtils.parseInt(str));
    }

    public long parseLong(String str) {
        return ensure(ParseUtils.parseLong(str));
    }

    public float parseFloat(String str) {
        return ensure(ParseUtils.parseFloat(str));
    }

    public Double parseDouble(String str) {
        return ensure(ParseUtils.parseDouble(str));
    }

    public BigDecimal parseBigDecimal(String str) {
        return ensure(ParseUtils.parseBigDecimal(str));
    }

    public <T> List<T> parseList(String str, @Nonnull Class<T> clazz) {
        return ensure(ParseUtils.parseList(str, clazz));
    }

    public <T> T parseObject(String str, @Nonnull Class<T> clazz) {
        T result = ParseUtils.parseObject(str, clazz);
        return result != null ? result : get(clazz::newInstance);
    }

    public <T> Stream<T> stream(Collection<T> collection) {
        return collection != null ? collection.stream().filter(Objects::nonNull) : Stream.empty();
    }

    public <T> Stream<T> stream(T[] array) {
        return array != null ? Arrays.stream(array).filter(Objects::nonNull) : Stream.empty();
    }

    public <T> Stream<T> validStream(Collection<T> collection) {
        return stream(collection).filter(CheckUtils::isValid);
    }

    public <T> Stream<T> validStream(T[] array) {
        return stream(array).filter(CheckUtils::isValid);
    }




    /**
     * 用于管理safer
     * @author LiYan
     */
    public static class SaferManager {

        private static final Map<Class<?>, SafeOperator> SAFER_MAP = new HashMap<>();

        /**
         * 创建新的Safer
         * @return 创建的Safer
         */
        static SafeOperator newSafer() {
            return new SafeOperator();
        }

        /**
         * 获取或生成对应的Safer示例
         * @param clazz 用户区分Safer的类
         * @return 获取或创建的Safer
         */
        public static SafeOperator saferFor(Class<?> clazz) {
            return SAFER_MAP.computeIfAbsent(clazz, k -> newSafer());
        }

        /**
         * 创建新的Safer，并添加异常处理器
         * @param clazz 用于区分Safer的类
         * @param handlers 异常处理器
         * @return 创建的Safer
         */
        public static SafeOperator newSaferWithThrowableHandlers(Class<?> clazz, SafeExceptionHandler... handlers) {
            return newSaferWithThrowableHandlers(clazz, Arrays.asList(handlers));
        }
        /**
         * 创建新的Safer，并添加异常处理器
         * @param clazz 用于区分Safer的类
         * @param handlers 异常处理器
         * @return 创建的Safer
         */
        public static SafeOperator newSaferWithThrowableHandlers(Class<?> clazz, List<SafeExceptionHandler> handlers) {
            SafeOperator safer = saferFor(clazz);
            safer.exceptionHandlers.addAll(handlers);
            return safer;
        }

        /**
         * 为Safer增加异常处理器
         * @param clazz 用于区分Safer的类
         * @param handlers 异常处理器
         */
        public static void addSaferExceptionHandlers(Class<?> clazz, SafeExceptionHandler... handlers) {
            addSaferExceptionHandlers(clazz, Arrays.asList(handlers));
        }

        /**
         * 为Safer增加异常处理器
         * @param clazz 用于区分Safer的类
         * @param handlers 异常处理器
         */
        public static void addSaferExceptionHandlers(Class<?> clazz, List<SafeExceptionHandler> handlers) {
            saferFor(clazz).exceptionHandlers.addAll(handlers);
        }


    }

}
