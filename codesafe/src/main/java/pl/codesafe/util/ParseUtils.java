package pl.codesafe.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import pl.codesafe.Opt;
import pl.codesafe.SafeOperator;
import pl.codesafe.SafeOperator.SaferManager;
import pl.codesafe.Safer;
import pl.codesafe.enums.ClassEnum;

/**
 * 常用解析、转换工具
 * <hr>
 * <li>所有以parse开头的方法，在解析失败或异常时，均返回null</li>
 * <li>所有以opt开头的方法，都是对parse方法的封装，用于无法直接接收null的情况</li>
 * @see Opt
 * @author LiYan
 */
public class ParseUtils {

    private static final SafeOperator SAFER = SaferManager.saferFor(ParseUtils.class);


    /**
     * Object 转 String
     * @param o object
     * @return String
     */
    @Nullable
    public static String parseStr(Object o) {
        return o == null ? null : o.toString();
    }

    /**
     * Object 转 String，并返回Opt
     * @param o object
     * @return Opt&lt;String&gt;
     * @see Opt
     */
    public static Opt<String> optStr(Object o) {
        return Opt.of(parseStr(o));
    }

    /**
     * Object 转 Boolean，兼容各种奇怪的能表达布尔值的语义
     * <hr>
     * <p>以下情况被视为true，字符串忽略大小写</p>
     * <li>true or "true"</li>
     * <li>1 or "1"</li>
     * <li>"T"</li>
     * <li>"YES" or "Y"</li>
     * <li>"ON"</li>
     * <p>以下情况被视为false，字符串忽略大小写</p>
     * <li>false or "false"</li>
     * <li>0 or "0"</li>
     * <li>"F"</li>
     * <li>"NO" or "N"</li>
     * <li>"OFF"</li>
     * <p>!!!如果不符合以上条件，返回null!!!</p>
     * @param o object
     * @return 如果不符合true或false的条件，返回null
     */
    @Nullable
    public static Boolean parseBoolean(Object o) {
        if (o == null) { return null; }
        if (o instanceof Boolean) { return (Boolean) o; }
        if (o instanceof String) {
            if ("true".equalsIgnoreCase((String) o)
                || "1".equalsIgnoreCase((String) o)
                || "T".equalsIgnoreCase((String) o)
                || "YES".equalsIgnoreCase((String) o)
                || "Y".equalsIgnoreCase((String) o)
                || "ON".equalsIgnoreCase((String) o)
            ) { return true; }
            if ("false".equalsIgnoreCase((String) o)
                || "0".equalsIgnoreCase((String) o)
                || "F".equalsIgnoreCase((String) o)
                || "NO".equalsIgnoreCase((String) o)
                || "N".equalsIgnoreCase((String) o)
                || "OFF".equalsIgnoreCase((String) o)
            ) { return false; }
        }
        if (o instanceof Number) {
            if (((Number) o).intValue() == 1) { return true; }
            if (((Number) o).intValue() == 0) { return false; }
        }
        return null;
    }

    /**
     * Object 转 Double，并返回Opt
     * @param o object
     * @return Opt&lt;Boolean&gt;
     * @see Opt
     */
    public static Opt<Boolean> optBoolean(Object o) {
        return Opt.of(parseBoolean(o));
    }

    /**
     * String 转 Byte
     * @param s string
     * @return 如果有任何异常，返回null
     */
    @Nullable
    public static Byte parseByte(String s) {
        return SAFER.get(() -> Byte.parseByte(s));
    }

    /**
     * Object 转 Byte，并返回Opt
     * @param s string
     * @return Opt&lt;Byte&gt;
     * @see Opt
     */
    public static Opt<Byte> optByte(String s) {
        return Opt.of(parseByte(s));
    }

    /**
     * String 转 Short
     * @param s string
     * @return 如果有任何异常，返回null
     */
    @Nullable
    public static Short parseShort(String s) {
        return SAFER.get(() -> Short.parseShort(s));
    }

    /**
     * Object 转 Short，并返回Opt
     * @param s string
     * @return Opt&lt;Short&gt;
     * @see Opt
     */
    public static Opt<Short> optShort(String s) {
        return Opt.of(parseShort(s));
    }

    /**
     * String 转 Double
     * @param s string
     * @return 如果有任何异常，返回null
     */
    @Nullable
    public static Double parseDouble(String s) {
        return SAFER.get(() -> Double.parseDouble(s));
    }

    /**
     * String 转 Double
     * @param s string
     * @return Opt&lt;Double&gt;
     * <p>
     * <p>optDouble("1.2").orElse(1.0D); // 1.2D
     * <p>optDouble("str").orElse(1.0D); // 1.0D
     * <p>optDouble(null).orElse(0.0D); // 0.0D
     * <p>optDouble("-1.0").filter(s -> s > 0).orElse(0.0D); // 0.0D
     *
     * @see Opt
     */
    public static Opt<Double> optDouble(String s) {
        return Opt.of(parseDouble(s));
    }

    /**
     * String 转 Float
     * @param s string
     * @return 如果有任何异常，返回null
     */
    @Nullable
    public static Float parseFloat(String s) {
        return SAFER.get(() -> Float.parseFloat(s));
    }

    /**
     * String 转 Float
     * @param s string
     * @return Opt&lt;Float&gt;
     * <p>
     * <p>optFloat("1.2").orElse(1.0F); // 1.2F
     * <p>optFloat("str").orElse(1.0F); // 1.0F
     * <p>optFloat(null).orElse(0.0F; // 0.0F
     * <p>optFloat("-1.0").filter(s -> s > 0).orElse(0.0F); // 0.0F
     *
     * @see Opt
     */
    public static Opt<Float> optFloat(String s) {
        return Opt.of(parseFloat(s));
    }

    /**
     * String 转 Integer
     * @param s string
     * @return 如果有任何异常，返回null
     */
    @Nullable
    public static Integer parseInt(String s) {
        return SAFER.get(() -> Integer.parseInt(s));
    }

    /**
     * String 转 Integer
     * @param s string
     * @return Opt&lt;Integer&gt;
     * <p>
     * <p>optInt("1").orElse(0); // 1
     * <p>optInt("str").orElse(0); // 0
     * <p>optInt(null).orElse(-1); // -1
     * <p>optInt("-1").filter(s -> s > 0).orElse(0); // 0
     *
     * @see Opt
     */
    public static Opt<Integer> optInt(String s) {
        return Opt.of(parseInt(s));
    }

    /**
     * String 转 Long
     * @param s string
     * @return 如果有任何异常，返回null
     */
    @Nullable
    public static Long parseLong(String s) {
        return SAFER.get(() -> Long.parseLong(s));
    }

    /**
     * String 转 Long
     *
     * @param s string
     * @return Opt&lt;Long&gt;
     * <p>
     * <p>optLong("1").orElse(0L); // 1L
     * <p>optLong("str").orElse(0L); // 0L
     * <p>optLong(null).orElse(-1L); // -1L
     * <p>optLong("-1").filter(s -> s > 0).orElse(0L); // 0L
     *
     * @see Opt
     */
    public static Opt<Long> optLong(String s) {
        return Opt.of(parseLong(s));
    }

    /**
     * String 转 BigDecimal
     * @param s string
     * @return 如果有任何异常，返回null
     */
    @Nullable
    public static BigDecimal parseBigDecimal(String s) {
        return SAFER.get(() -> new BigDecimal(s));
    }

    /**
     * String 转 BigDecimal
     *
     * @param s string
     * @return Opt&lt;BigDecimal&gt;
     *
     * @see Opt
     */
    public static Opt<BigDecimal> optBigDecimal(String s) {
        return Opt.of(parseBigDecimal(s));
    }

    /**
     * collection 转 HashMap
     * @param collection 集合
     * @param keyFun 提取Key的函数
     * @param valueFun 提取Value的函数
     * @param <T> 类型
     * @param <K> Key的类型
     * @param <V> Value的类型
     */
    public static <T, K, V> Map<K, V> parseMap(Collection<T> collection, Function<T, K> keyFun, Function<T, V> valueFun) {
        return Safer.stream(collection).collect(Collectors.toMap(keyFun, valueFun, (oldVal, newVal) -> newVal, HashMap::new));
    }

    /**
     * collection 转 LinkedHashMap，需要转换后的Map保留集合的顺序的话，推荐使用LinkedHashMap
     * @param collection 集合
     * @param keyFun 提取Key的函数
     * @param valueFun 提取Value的函数
     * @param <T> 类型
     * @param <K> Key的类型
     * @param <V> Value的类型
     */
    public static <T, K, V> Map<K, V> parseOrderedMap(Collection<T> collection, Function<T, K> keyFun, Function<T, V> valueFun) {
        return Safer.stream(collection).collect(Collectors.toMap(keyFun, valueFun, (oldVal, newVal) -> newVal, LinkedHashMap::new));
    }

    /**
     * json转对象
     * <p>使用前需要配置{@link ParseUtilsManager#setJsonObjectParser(BiFunction)}</p>
     */

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T parseObject(String str, @Nonnull Class<T> clazz) {
        return (T) SAFER.get(() -> ParseUtilsManager.getJsonObjectParser().apply(str, clazz));
    }

    /**
     * json转List
     * <p>使用前需要配置{@link ParseUtilsManager#setJsonArrayParser(BiFunction)}</p>
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> parseList(String str, @Nonnull Class<T> clazz) {
        return SAFER.getList(() -> (List<T>) ParseUtilsManager.getJsonArrayParser().apply(str, clazz));
    }

    /**
     * 尝试根据指定类型去解析字符串
     * @param str   要解析的字符串
     * @param clazz 解析成此类
     * @param <T>   解析后的类型
     * @return 解析后的对象，解析失败或异常返回null
     */
    @Nullable
    public static <T> T parse(String str, @Nonnull Class<T> clazz) {
        return parse(str, ClassEnum.fromClass(clazz), clazz);
    }

    /**
     * 尝试根据指定类型去解析字符串
     * @param str       要解析的字符串
     * @param classEnum 类型枚举
     * @param clazz     classEnum为Object时，使用json反序列化成此类
     * @param <T>       解析后的类型
     * @return 解析后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(String str, @Nonnull ClassEnum classEnum, Class<T> clazz) {
        switch (classEnum) {
            case BOOLEAN:
                return (T) parseBoolean(str);
            case STRING:
                return (T) str;
            case BYTE:
                return (T) parseByte(str);
            case SHORT:
                return (T) parseShort(str);
            case INTEGER:
                return (T) parseInt(str);
            case LONG:
                return (T) parseLong(str);
            case FLOAT:
                return (T) parseFloat(str);
            case DOUBLE:
                return (T) parseDouble(str);
            case BIG_DECIMAL:
                return (T) parseBigDecimal(str);
            case OBJECT:
                return parseObject(str, clazz);
            default:
                return null;
        }
    }


    /**
     * Collection转List
     * @param origin 原集合
     * @param mapper 每个元素的转换方法
     * @param <E> 集合元素类型
     * @param <N> 转换后的元素类型
     * @return 转换后的List
     */
    public static <E, N> List<N> mapList(Collection<E> origin, Function<E, N> mapper) {
        return Safer.stream(origin).map(mapper).collect(Collectors.toList());
    }

    /**
     * Map转List
     * @param originMap 原Map
     * @param mapper 每组键值对转换为列表元素的方法
     * @param <K> 键类型
     * @param <V> 值类型
     * @param <N> 列表元素的类型
     * @return 转换后的List
     */
    public static <K, V, N> List<N> parseList(Map<K, V> originMap, BiFunction<K, V, N> mapper) {
        return Safer.stream(originMap).map(e -> mapper.apply(e.getKey(), e.getValue())).collect(Collectors.toList());
    }



}
