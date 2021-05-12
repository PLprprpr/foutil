package pl.codesafe;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import pl.abstracts.functions.RunnableWithThrowable;
import pl.abstracts.functions.SupplierWithThrowable;
import pl.codesafe.SafeOperator.SaferManager;

/**
 * <code>SafeOperator</code>的默认静态实例
 * @author LiYan
 * @see SafeOperator
 */
public class Safer {

    private static final SafeOperator SAFER = SaferManager.saferFor(Safer.class);

    /**
     * 安全执行某个函数，忽略其中所有异常
     * @param runnable 执行函数
     */
    public static <EX extends Throwable> void execute(@Nonnull RunnableWithThrowable<EX> runnable) {
        SAFER.execute(runnable);
    }

    /**
     * 安全执行某个取值函数，忽略其中所有异常，返回执行得到的值，失败时返回null
     * @param supplier 取值函数
     * @return 执行函数的返回值，执行失败时返回null
     */
    public static <T, EX extends Throwable> T get(@Nonnull SupplierWithThrowable<T, EX> supplier) {
        return SAFER.get(supplier);
    }

    public static boolean ensure(Boolean value) {
        return SAFER.ensure(value);
    }

    public static String ensure(String value) {
        return SAFER.ensure(value);
    }

    public static byte ensure(Byte value) {
        return SAFER.ensure(value);
    }

    public static short ensure(Short value) {
        return SAFER.ensure(value);
    }

    public static int ensure(Integer value) {
        return SAFER.ensure(value);
    }

    public static long ensure(Long value) {
        return SAFER.ensure(value);
    }

    public static float ensure(Float value) {
        return SAFER.ensure(value);
    }

    public static double ensure(Double value) {
        return SAFER.ensure(value);
    }

    public static BigDecimal ensure(BigDecimal value) {
        return SAFER.ensure(value);
    }

    public static <E> List<E> ensure(List<E> value) {
        return SAFER.ensure(value);
    }

    public static <E> Set<E> ensure(Set<E> value) {
        return SAFER.ensure(value);
    }

    public static <K, V> Map<K, V> ensure(Map<K, V> value) {
        return SAFER.ensure(value);
    }

    public static <EX extends Throwable> boolean getBool(@Nonnull SupplierWithThrowable<Boolean, EX> supplier) {
        return SAFER.getBool(supplier);
    }

    public static <EX extends Throwable> String getStr(@Nonnull SupplierWithThrowable<String, EX> supplier) {
        return SAFER.getStr(supplier);
    }

    public static <EX extends Throwable> byte getByte(@Nonnull SupplierWithThrowable<Byte, EX> supplier) {
        return SAFER.getByte(supplier);
    }

    public static <EX extends Throwable> short getShort(@Nonnull SupplierWithThrowable<Short, EX> supplier) {
        return SAFER.getShort(supplier);
    }

    public static <EX extends Throwable> int getInt(@Nonnull SupplierWithThrowable<Integer, EX> supplier) {
        return SAFER.getInt(supplier);
    }

    public static <EX extends Throwable> long getLong(@Nonnull SupplierWithThrowable<Long, EX> supplier) {
        return SAFER.getLong(supplier);
    }

    public static <EX extends Throwable> float getFloat(@Nonnull SupplierWithThrowable<Float, EX> supplier) {
        return SAFER.getFloat(supplier);
    }

    public static <EX extends Throwable> Double getDouble(@Nonnull SupplierWithThrowable<Double, EX> supplier) {
        return SAFER.getDouble(supplier);
    }

    public static <EX extends Throwable> BigDecimal getBigDecimal(@Nonnull SupplierWithThrowable<BigDecimal, EX> supplier) {
        return SAFER.getBigDecimal(supplier);
    }

    public static <E, EX extends Throwable> List<E> getList(@Nonnull SupplierWithThrowable<List<E>, EX> supplier) {
        return SAFER.getList(supplier);
    }

    public static <E, EX extends Throwable> Set<E> getSet(@Nonnull SupplierWithThrowable<Set<E>, EX> supplier) {
        return SAFER.getSet(supplier);
    }

    public static <K, V, EX extends Throwable> Map<K, V> getMap(@Nonnull SupplierWithThrowable<Map<K, V>, EX> supplier) {
        return SAFER.getMap(supplier);
    }

    public static String parseStr(Object o) {
        return SAFER.parseStr(o);
    }

    public static boolean parseBool(String str) {
        return SAFER.parseBool(str);
    }

    public static byte parseByte(String str) {
        return SAFER.parseByte(str);
    }

    public static short parseShort(String str) {
        return SAFER.parseShort(str);
    }

    public static int parseInt(String str) {
        return SAFER.parseInt(str);
    }

    public static long parseLong(String str) {
        return SAFER.parseLong(str);
    }

    public static float parseFloat(String str) {
        return SAFER.parseFloat(str);
    }

    public static Double parseDouble(String str) {
        return SAFER.parseDouble(str);
    }

    public static BigDecimal parseBigDecimal(String str) {
        return SAFER.parseBigDecimal(str);
    }

    public static <T> List<T> parseList(String str, @Nonnull Class<T> clazz) {
        return SAFER.parseList(str, clazz);
    }

    public static <T> T parseObject(String str, @Nonnull Class<T> clazz) {
        return SAFER.parseObject(str, clazz);
    }

    public static <T> Stream<T> stream(Collection<T> collection) {
        return SAFER.stream(collection);
    }

    public static <T> Stream<T> stream(T[] array) {
        return SAFER.stream(array);
    }

    /**
     * 安全的得到一个Map.Entry的stream
     * @return Map.Entry的stream或者当Map为null时返回一个空的stream
     */
    public static <K, V> Stream<Entry<K, V>> stream(Map<K, V> map) {
        return map != null ? map.entrySet().stream().filter(Objects::nonNull) : Stream.empty();
    }

    public static <T> Stream<T> validStream(Collection<T> collection) {
        return SAFER.validStream(collection);
    }

    public static <T> Stream<T> validStream(T[] array) {
        return SAFER.validStream(array);
    }
    
}
