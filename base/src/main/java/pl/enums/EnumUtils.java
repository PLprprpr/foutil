package pl.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * 枚举工具
 * @author LiYan
 */
public class EnumUtils {

    /**
     * 使用提供的索引构造方法，为枚举类建立索引
     * <hr><pre>
     * enum DemoEnum {
     *
     *     UNKNOWN(0, "unknown"),
     *     A(1, "a"),
     *     B(2, "b");
     *
     *     private int value;
     *     private String tag;
     *
     *     public DemoEnum(int value, String tag) {
     *         this.value = value;
     *         this.tag = tag;
     *     }
     *     public int getValue() { return value; }
     *     public String getTag() { return tag; }
     *
     *     // 建立value索引
     *     public static final EnumIndexCache&lt;Integer, DemoEnum&gt; valueIndex
     *          = EnumUtils.buildEnumIndex(DemoEnum.class, DemoEnum::getValue);
     *
     *     // 建立tag索引
     *     public static final EnumIndexCache&lt;String, DemoEnum&gt; tagIndex
     *          = EnumUtils.buildEnumIndex(DemoEnum.class, DemoEnum::getTag);
     * }</pre>
     * <hr><pre>
     * // 使用索引
     * DemoEnum.valueIndex.getEnumByIndex(2); // return DemoEnum.B
     * DemoEnum.valueIndex.getEnumByIndex(9); // return null
     * DemoEnum.tagIndex.getEnumByIndex("a", UNKNOWN); // return DemoEnum.A
     * DemoEnum.tagIndex.getEnumByIndex("z", UNKNOWN); // return DemoEnum.UNKNOWN
     * DemoEnum.tagIndex.getEnumByIndex("z", () -> new MyException()); // throw exception</pre>
     * <hr>
     *
     * <p>作为索引的字段必须在枚举值中不重复</p>
     * <p>可以给一个枚举类建立多个索引</p>
     * @param enumClass 枚举类
     * @param makeIndexFun 使用哪个字段建立索引
     * @param <T> 索引的类型
     * @param <E> 枚举的类型
     * @return 建立的索引缓存
     */
    public static <T, E extends Enum<E>> EnumIndexCache<T, E> buildEnumIndex(Class<E> enumClass, Function<E, T> makeIndexFun) {
        Map<T, E> map = Arrays.stream(enumClass.getEnumConstants()).collect(
            Collectors.toMap(makeIndexFun, Function.identity(), (oldVal, newVal) -> {
                throw new RuntimeException(
                    String.format("Enum index must be unique, but found multiple enum: %s and %s", oldVal.name(), newVal.name()));
            }));
        return new EnumIndexCache<>(map);
    }

    public static class EnumIndexCache<T, E extends Enum<E>> {

        private final Map<T, E> cacheMap;

        public EnumIndexCache(Map<T, E> map) {
            cacheMap = Collections.unmodifiableMap(map);
        }

        @Nullable
        public E getEnumByIndex(T index) {
            return index == null ? null : cacheMap.get(index);
        }

        public E getEnumByIndex(T index, E defaultEnum) {
            return index == null ? defaultEnum : cacheMap.getOrDefault(index, defaultEnum);
        }

        public <X extends Throwable> E getEnumByIndex(T index, Supplier<X> throwableSupplier) throws X {
            E e = getEnumByIndex(index);
            if (e == null) {
                throw  throwableSupplier.get();
            }
            return e;
        }
    }

}
