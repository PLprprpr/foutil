package pl.codesafe;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import pl.abstracts.functions.FunctionWithThrowable;
import pl.codesafe.SafeOperator.SaferManager;

/**
 * Better optional
 * @author LiYan
 * @see Optional
 */
public class Opt<T> {

    private static final SafeOperator SAFER = SaferManager.saferFor(Opt.class);

    /**
     * 空值单例 {@code empty()}.
     */
    private static final Opt<?> EMPTY = new Opt<>();

    /**
     * 如果非null，则为value;如果为null，则表示不存在任何值
     */
    private final T value;

    /**
     * 构造一个空实例。
     *
     * @implNote 通常只有一个空实例{@link Opt＃EMPTY}，
     * 每个VM应该只存在一个。
     */
    private Opt() {
        this.value = null;
    }

    /**
     * 返回一个空的{@code Opt}实例。
     */
    public static<T> Opt<T> empty() {
        @SuppressWarnings("unchecked")
        Opt<T> t = (Opt<T>) EMPTY;
        return t;
    }

    /**
     * 构造实例。
     * @param value 非空值
     * @throws NullPointerException if value is null
     */
    private Opt(T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * 返回具有指定的当前非空值的{@code Opt}。
     *
     * @param value 必须为非null
     * @return 包装value值的{@code Opt}
     * @throws NullPointerException 如果value为null
     */
    public static <T> Opt<T> check(T value) {
        return new Opt<>(value);
    }

    /**
     * 返回描述指定值的{@code Opt}，如果值为null，则返回空{@code Opt}。
     */
    public static <T> Opt<T> of(T value) {
        return value == null ? empty() : check(value);
    }

    /**
     * 返回描述指定Optional中的值的{@code Opt}，如果值为null，则返回空{@code Opt}。
     */
    public static <T> Opt<T> of(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> optional) {
        return optional.map(Opt::check).orElseGet(Opt::empty);
    }

    /**
     * 禁止外部使用
     */
    private T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * 如果存在值，则使用值调用指定的consumer，否则不执行任何操作。
     */
    public Opt<T> ifPresent(Consumer<? super T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
        return this;
    }

    /**
     * 如果不存在值，则调用指定的runnable，否则不执行任何操作。
     */
    public Opt<T> ifAbsent(Runnable runnable) {
        if (value == null) {
            runnable.run();
        }
        return this;
    }

    /**
     * 如果存在值，并且值符合判断规则，返回描述值的{@code Opt}，否则返回空{@code Opt}。
     */
    public Opt<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        } else {
            return SAFER.getBool(() -> predicate.test(value)) ? this : empty();
        }
    }

    /**
     * <p>以字段值过滤原值</p>
     * 如果存在值，并且它的指定字段值符合判断规则，返回描述原值的{@code Opt}（不会转换为字段），否则返回空{@code Opt}。
     */
    public <R> Opt<T> filterField(Function<T, R> getFieldFun, Predicate<? super R> predicate) {
        if (!isPresent()) {
            return this;
        } else {
            return SAFER.getBool(() -> predicate.test(getFieldFun.apply(value))) ? this : empty();
        }
    }

    /**
     * 如果存在值，并且值!不!符合判断规则，返回描述值的{@code Opt}，否则返回空{@code Opt}。
     * @see Opt#filter(Predicate)
     */
    public Opt<T> drop(Predicate<? super T> predicate) {
        return filter(predicate.negate());
    }

    /**
     * <p>以字段值反过滤原值</p>
     * 如果存在值，并且它的指定字段!不!符合判断规则，返回描述值的{@code Opt}，否则返回空{@code Opt}。
     */
    public <R> Opt<T> dropField(Function<T, R> getFieldFun, Predicate<? super R> predicate) {
        return filterField(getFieldFun, predicate.negate());
    }

    /**
     * 如果存在值，则将提供的映射函数应用于该值，如果结果为非null，则返回描述该结果的{@code Opt}。否则返回一个空{@code Opt}。
     */
    public <U> Opt<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            return Opt.of(mapper.apply(value));
        }
    }

    /**
     * 在map的基础上加了try catch，如果map方法出现异常，忽略并返回empty
     */
    public <U, EX extends Throwable> Opt<U> safeMap(FunctionWithThrowable<? super T, ? extends U, EX> mapper) {
        try {
            return map(mapper.toFunction());
        } catch (Throwable e) {
            SAFER.handleException(e);
            return empty();
        }
    }

    /**
     * 如果存在值，将应用提供的{@code Opt}
     * 映射函数到它，返回结果，否则返回空
     * {@code Opt}。此方法类似于{@link #map(Function)}，
     * 但提供的映射器是一个结果已经是{@code Opt}的映射器。
     *
     * @throws NullPointerException 如果映射函数为null或返回空结果
     */
    public <U> Opt<U> mapOpt(Function<? super T, Opt<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            return Objects.requireNonNull(mapper.apply(value));
        }
    }

    /**
     * 如果存在值，将应用提供的{@code Optional}
     * 映射函数到它，返回结果，否则返回空
     * {@code Opt}。此方法类似于{@link #map(Function)}，
     * 但提供的映射器是一个结果已经是{@code Optional}的映射器。
     *
     * @throws NullPointerException 如果映射函数为null或返回空结果
     */
    public <U> Opt<U> mapOptional(Function<? super T, Optional<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            return Opt.of(mapper.apply(value));
        }
    }

    /**
     * 返回包含的值（如果存在），否则返回null
     */
    public final T orNull() {
        return isPresent() ? value : null;
    }

    /**
     * 返回包含的值（如果存在），否则返回{@code others}中第一个不为null的值。
     */
    @SafeVarargs
    public final T orElse(T... others) {
        T result = value;
        if (result == null) {
            for (T other : others) {
                if (other != null) {
                    return other;
                }
            }
        }
        return result;
    }

    /**
     * 返回包含的值（如果存在），否则依次调用{@code others}并返回第一个不为null的调用结果。
     */
    @SafeVarargs
    public final T orElse(Supplier<? extends T>... others) {

        T result = value;
        if (result == null && others != null) {
            for (Supplier<? extends T> other : others) {
                result = other.get();
                if (result != null) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * 返回当前Opt（如果值存在），否则返回描述{@code other}的Opt。
     */
    public Opt<T> orWrap(T other) {
        return isPresent() ? this : Opt.of(other);
    }

    /**
     * 返回当前Opt（如果值存在），否则调用{@code other}并返回描述调用结果的Opt。
     */
    public Opt<T> orWrap(@Nonnull Supplier<? extends T> other) {
        return isPresent() ? this : Opt.of(other.get());
    }

    /**
     * 返回当前Opt（如果值存在），否则返回other Opt。
     */
    public Opt<T> orUse(@Nonnull Opt<T> other) {
        return isPresent() ? this : other;
    }

    /**
     * 返回当前Opt（如果值存在），否则调用{@code other}获得新的Opt并返回。
     */
    public Opt<T> orUse(@Nonnull Supplier<? extends Opt<T>> other) {
        return isPresent() ? this : other.get();
    }

    /**
     * 返回包含的值（如果存在），否则抛出提供的异常。
     */
    public <X extends Throwable> T orElseThrow(@Nonnull Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 返回当前Opt（如果值存在），否则抛出提供的异常。
     */
    public <X extends Throwable> Opt<T> orThrow(@Nonnull Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return this;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 指示某个其他对象是否“等于”此Opt。
     * 如果符合以下条件，则认为相等：
     * <ul>
     * <li>它也是{@code Opt} 并且;
     * <li>两个实例都没有值 或者;
     * <li>当前值通过{@code equals（）}“相等”。
     * </ul>
     *
     * @param obj an object to be tested for equality
     * @return {@code true} 如果另一个对象“等于”此对象，否则{@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Opt)) {
            return false;
        }

        Opt<?> other = (Opt<?>) obj;
        return Objects.equals(value, other.value);
    }

    /**
     * 返回当前值的哈希码值（如果有）或 0（没有值时）。
     *
     * @return 当前值的哈希码值，如果不存在值则为0
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value != null
               ? String.format("Opt[%s]", value)
               : "Opt.empty";
    }


}
