package pl.codesafe.enums;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import pl.enums.EnumUtils;
import pl.enums.EnumUtils.EnumIndexCache;

/**
 * @author LiYan
 */
public enum ClassEnum {

    /**
     * 布尔型
     */
    BOOLEAN(Boolean.class),
    /**
     * 字符串
     */
    STRING(String.class),

    /**
     * 字节
     */
    BYTE(Byte.class),

    /**
     * 短整型
     */
    SHORT(Short.class),

    /**
     * 整型
     */
    INTEGER(Integer.class),

    /**
     * 长整型
     */
    LONG(Long.class),

    /**
     * 单精度浮点型
     */
    FLOAT(Float.class),

    /**
     * 双精度浮点型
     */
    DOUBLE(Double.class),

    /**
     * 复合数字型
     */
    BIG_DECIMAL(BigDecimal.class),

    /**
     * 列表
     */
    LIST(List.class),

    /**
     * Map
     */
    MAP(Map.class),










    OBJECT(Object.class)
    ;


    private final Class<?> clazz;

    public Class<?> getClazz() {
        return clazz;
    }

    ClassEnum(Class<?> clazz) {
        this.clazz = clazz;
    }

    private static final EnumIndexCache<Class<?>, ClassEnum> index = EnumUtils.buildEnumIndex(ClassEnum.class, ClassEnum::getClazz);

    public static ClassEnum fromClass(Class<?> clazz) {
        return index.getEnumByIndex(clazz, OBJECT);
    }

    public boolean valueEquals(Class<?> clazz) {
        return this.clazz.equals(clazz);
    }

}
