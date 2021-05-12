package pl.codesafe;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import pl.abstracts.functions.SupplierWithThrowable;
import pl.codesafe.SafeOperator.SaferManager;

/**
 * 定义一个默认值和校验规则，如果获取的值未通过校验，返回默认值
 * @author LiYan
 */
public class Def<T> {


    private final T defaultValue;

    private final Predicate<T> tester;

    private Def(T defaultValue) {
        this.defaultValue = defaultValue;
        this.tester = Objects::nonNull;
    }

    private Def(T defaultValue, Predicate<T> tester) {
        this.defaultValue = defaultValue;
        this.tester = tester;
    }

    private static final SafeOperator SAFER = SaferManager.saferFor(Def.class);

    /**
     * 构造一种Def，传入自定义默认值。当Def取值为null时，会自动返回默认值
     * @param defaultValue 默认值
     * @param <T> Def的类型
     * @return 构造得到Def
     */
    public static <T> Def<T> of(T defaultValue) {
        return new Def<>(defaultValue);
    }

    /**
     * 构造一种Def，传入自定义默认值和校验函数。当Def取到的值没有通过校验函数时，会自动返回默认值
     * @param defaultValue 默认值
     * @param tester 校验函数
     * @param <T> Def的类型
     * @return 构造得到Def
     */
    public static <T> Def<T> of(T defaultValue, @Nonnull Predicate<T> tester) {
        return new Def<>(defaultValue, tester);
    }

    /**
     * 设置校验器，会返回带有校验器的新Def
     * @param tester 校验函数
     * @return 新的Def
     */
    public Def<T> filter(@Nonnull Predicate<T> tester) {
        return Def.of(this.defaultValue, tester);
    }

    /**
     * 获取默认值
     * @return 默认值
     */
    public T defaultValue() {
        return defaultValue;
    }

    /**
     * 获取值，如果获取时出错或者取到的值无法通过test，返回默认值
     * @param supplier 取值函数
     * @return 取到的值或默认值
     */
    public <EX extends Throwable> T get(@Nonnull SupplierWithThrowable<T, EX> supplier) {
        try {
            T value = supplier.get();
            if (tester.test(value)) {
                return value;
            }
        } catch (Throwable t) {
            SAFER.handleException(t);
        }
        return defaultValue;
    }

    /**
     * 获取值，如果该值无法通过test，返回默认值
     * @param value 取的值
     * @return 读到的值或默认值
     */
    public T get(T value) {
        try {
            if (tester.test(value)) {
                return value;
            }
        } catch (Throwable t) {
            SAFER.handleException(t);
        }
        return defaultValue;
    }

    /**
     * 获取值，如果获取时出错或者取到的值无法通过test，返回null
     * @param supplier 取值函数
     * @return 取到的值或null
     */
    @Nullable
    public <EX extends Throwable> T checkOrNull(@Nonnull SupplierWithThrowable<T, EX> supplier) {
        try {
            T value = supplier.get();
            if (tester.test(value)) {
                return value;
            }
        } catch (Throwable t) {
            SAFER.handleException(t);
        }
        return null;
    }

    /**
     * 获取值，如果该值无法通过test，返回null
     * @param value 取的值
     * @return 读到的值或null
     */
    @Nullable
    public T checkOrNull(T value) {
        try {
            if (tester.test(value)) {
                return value;
            }
        } catch (Throwable t) {
            SAFER.handleException(t);
        }
        return null;
    }



    public static final Def<Boolean> BOOLEAN = Def.of(false);
    public static final Def<String> STRING = Def.of("");
    public static final Def<Byte> BYTE = Def.of((byte) 0);
    public static final Def<Short> SHORT = Def.of((short) 0);
    public static final Def<Integer> INTEGER = Def.of(0);
    public static final Def<Long> LONG = Def.of(0L);
    public static final Def<Float> FLOAT = Def.of(0F);
    public static final Def<Double> DOUBLE = Def.of(0D);
    public static final Def<BigDecimal> BIG_DECIMAL = Def.of(BigDecimal.ZERO);


}
