package pl.codesafe.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import pl.abstracts.validate.Validatable;
import pl.codesafe.Safer;

/**
 * 校验参数工具
 * @author LiYan
 */
public class CheckUtils {



    /*************************************** Object ******************************************/

    public static <T> boolean isNull(T object) {
        return null == object;
    }

    public static boolean isAnyNull(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (Object object : objects) {
            if (isNull(object)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean isNotNull(T object) {
        return !isNull(object);
    }

    public static boolean isNotNull(Object... objects) {
        return !isAnyNull(objects);
    }

    /**
     * 是否是“真” （广义）
     * @param o any object
     * @return o in [true, "true", 1, "1", "T", "Y", "YES"]
     */
    public static boolean isTrue(Object o) {
        return ParseUtils.optBoolean(o).orElse(false);
    }

    /**
     * 是否是“假” （广义）
     * @param o any object
     * @return o not in [true, "true", 1, "1", "T", "Y", "YES"]
     * @see #isTrue(Object)
     */
    public static boolean isFalse(Object o) {
        return !isTrue(o);
    }


    /*************************************** String ******************************************/

    /**
     * 字符串是否为空（广义）
     * @param s string
     * @return 字符串为 null, "", " ", "null", "undefined" 时，true
     */
    public static boolean isBlank(String s) {
        return s == null || s.length() == 0 || "null".equalsIgnoreCase(s) || "undefined".equalsIgnoreCase(s);
    }

    /**
     * 字符串是否不为空（广义）
     * @param s string
     * @return 字符串为 null, "", " ", "null", "undefined" 时，false
     * @see #isBlank(String)
     */
    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    /**
     * 这些字符串是否存在空（广义）
     * @param strs string[]
     * @return 某个字符串为 null, "", " ", "null", "undefined" 时，true
     * @see #isBlank(String)
     */
    public static boolean isAnyBlank(String... strs) {
        if (strs == null) {
            return true;
        }
        for (String s : strs) {
            if (isBlank(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllBlank(String... strs) {
        if (strs == null) {
            return true;
        }
        for (String s : strs) {
            if (isNotBlank(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串是否都不为空（广义）
     * @param strs string[]
     * @return ! isAnyBlank
     * @see #isAnyBlank
     */
    public static boolean isAllNotBlank(String... strs) {
        return !isAnyBlank(strs);
    }



    public static boolean isAnyNotBlank(String... strs) {
        return !isAllNotBlank(strs);
    }

    /*************************************** Collection ******************************************/

    /**
     * 集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 集合是否不为空
     * @return ! isEmpty
     * @see #isEmpty(Collection)
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /*************************************** Map ******************************************/

    /**
     * Map是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Map是否不为空
     * @return ! isEmpty
     * @see #isEmpty(Map)
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /*************************************** Number ******************************************/

    /**
     * 是否等于0 (number.doubleValue() == 0)
     */
    public static boolean equalsZero(Number number) {
        if (number != null) {
            return number.doubleValue() == 0;
        }
        return false;
    }

    /**
     * 是否大于0 (number.doubleValue() > 0)
     */
    public static boolean moreThanZero(Number number) {
        if (number != null) {
            return number.doubleValue() > 0;
        }
        return false;
    }

    /**
     * 是否大于等于0 (number.doubleValue() >= 0)
     */
    public static boolean moreEqualsZero(Number number) {
        if (number != null) {
            return number.doubleValue() >= 0;
        }
        return false;
    }

    /**
     * 是否小于0 (number.doubleValue() < 0)
     */
    public static boolean lessThanZero(Number number) {
        if (number != null) {
            return number.doubleValue() < 0;
        }
        return false;
    }

    /**
     * 是否小于等于0 (number.doubleValue() <= 0)
     */
    public static boolean lessEqualsZero(Number number) {
        if (number != null) {
            return number.doubleValue() <= 0;
        }
        return false;
    }

    /**
     * 两个BigDecimal是否相等
     */
    public static boolean isEqual(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        if (isAnyNull(bigDecimal1, bigDecimal2)) {
            return false;
        }
        return bigDecimal1.compareTo(bigDecimal2) == 0;
    }

    /**
     * 两个BigDecimal是否不相等
     * @return ! isEqual
     * @see #isEqual(BigDecimal, BigDecimal)
     */
    public static boolean isNotEqual(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return !isEqual(bigDecimal1, bigDecimal2);
    }

    /**
     * 是否有小数位
     * @return 是否有小数位
     */
    public static boolean hasDecimal(Number number) {
        if (isNull(number)) {
            return false;
        }
        if (number instanceof Integer || number instanceof Long || number instanceof Short || number instanceof Byte) {
            return false;
        }
        return Math.abs(number.doubleValue() % 1) > 0;
    }

    /*************************************** Time ******************************************/

    /**
     * 时间点1是否在时间点2之前
     * @param localDateTime 要校验的时间点
     * @param beforeThis    在这个时间点之前
     */
    public static boolean before(LocalDateTime localDateTime, LocalDateTime beforeThis) {

        if (localDateTime != null) {
            return localDateTime.isBefore(beforeThis);
        }
        return false;
    }

    /**
     * 时间点1是否在时间点2之后
     * @param localDateTime 要校验的时间点
     * @param afterThis     在这个时间点之后
     */
    public static boolean after(LocalDateTime localDateTime, LocalDateTime afterThis) {

        if (localDateTime != null) {
            return localDateTime.isAfter(afterThis);
        }
        return false;
    }

    /**
     * 时间点是否在Now()之前
     * @return before(localDateTime, LocalDateTime.now())
     */
    public static boolean beforeNow(LocalDateTime localDateTime) {
        return before(localDateTime, LocalDateTime.now());
    }

    /**
     * 时间点是否在Now()之后
     * @return after(localDateTime, LocalDateTime.now())
     */
    public static boolean afterNow(LocalDateTime localDateTime) {
        return after(localDateTime, LocalDateTime.now());
    }


    /*************************************** Valiatable ******************************************/

    /**
     * 是否为有效数据，判断有效数据的逻辑如下：
     * <p>(Any) isNotNull</p>
     * <p>(Validatable) valid()</p>
     * <p>(String) isNotBlank</p>
     * <p>(Collection) isNotEmpty</p>
     * <p>(Map) isNotEmpty</p>
     */
    public static <T> boolean isValid(T o) {
        // 基础判空
        if (isNull(o)) {
            return false;
        }
        // String
        if (o instanceof String) {
            return CheckUtils.isNotBlank((String) o);
        }
        // Collection
        if (o instanceof Collection) {
            return CheckUtils.isNotEmpty((Collection<?>) o);
        }
        // Map
        if (o instanceof Map) {
            return CheckUtils.isNotEmpty((Map<?, ?>) o);
        }
        // Validatable类
        if (o instanceof Validatable) {
            return ((Validatable) o).valid();
        }

        return true;
    }

    /**
     * 是否全部为有效数据，判断有效数据的逻辑如下：
     * <p>(Any) isNotNull</p>
     * <p>(Validatable) valid()</p>
     * <p>(String) isNotBlank</p>
     * <p>(Collection) isNotEmpty</p>
     */
    public static boolean isValid(Object... os) {
        if (isNull(os)) {
            return false;
        }
        for (Object o : os) {
            if (notValid(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为无效数据
     */
    public static <T> boolean notValid(T o) {
        return !isValid(o);
    }

    public static <T> boolean allElementValid(Iterable<T> iterable) {
        for (T e : iterable) {
            if (notValid(e)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean anyElementValid(Iterable<T> iterable) {
        for (T e : iterable) {
            if (isValid(e)) {
                return true;
            }
        }
        return false;
    }

    /*************************************** Field ******************************************/

    /**
     * 字段不为空
     */
    public static <T, R> Predicate<T> fieldNotNull(Function<T, R> getFieldFun) {
        return o -> CheckUtils.isNotNull(o) && CheckUtils.isNotNull(getFieldFun.apply(o));
    }

    /**
     * 多个字段不为空
     */
    @SafeVarargs
    public static <T, R> Predicate<T> fieldNotNull(Function<T, R>... getFieldFuns) {
        return Safer.stream(getFieldFuns).map(CheckUtils::fieldNotNull).reduce(Predicate::and).orElse(o -> Boolean.FALSE);
    }

    /**
     * 字段有效
     */
    public static <T, R> Predicate<T> fieldValid(Function<T, R> getFieldFun) {
        return o -> CheckUtils.isNotNull(o) && CheckUtils.isValid(getFieldFun.apply(o));
    }

    /**
     * 多个字段有效
     */
    @SafeVarargs
    public static <T, R> Predicate<T> fieldValid(Function<T, R>... getFieldFuns) {
        return Safer.stream(getFieldFuns).map(CheckUtils::fieldValid).reduce(Predicate::and).orElse(o -> Boolean.FALSE);
    }




    /*************************************** Any ******************************************/

    /**
     * 为自定义的校验器增加前置判空
     * <p>比如：CheckUtils.notNullAnd(ResultInfo::success)</p>
     * <p>等价于：result -> result != null && result.success()</p>
     * @param validator 自定义的校验器
     * @param <T> 类型
     * @return 增加前置判空的校验器
     */
    public static <T> Predicate<T> notNullAnd(Predicate<T> validator) {
        return o -> isNotNull(o) && validator.test(o);
    }

}
