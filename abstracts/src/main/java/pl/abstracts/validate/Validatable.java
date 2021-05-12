package pl.abstracts.validate;

/**
 * 通过定义接口，标识哪些类有自我检验的能力，适合那些校验较复杂且自我维护意识较强的类设计
 * <hr><pre>
 * public class Data implements Validatable {
 *     private long id;
 *     private String value;
 *     private int active;
 *     // getter setter ...;
 *
 *     @ Override
 *     public boolean valid() {
 *         return active == 1 && value != null;
 *     }
 * }</pre>
 * <hr><pre>
 * // 使用
 * public Data getFirstActiveDatas(List&lt;Data&gt; datas) {
 *     if(datas == null) { return null; }
 *     return datas.stream().filter(Validatable::check).findFirst().orElse(null);
 * }
 * </pre>
 * @author LiYan
 */
public interface Validatable {

    /**
     * 是否有效
     * @return valid
     */
    boolean valid();

    /**
     * 是否无效
     * @return !valid()
     */
    default boolean invalid() {
        return !valid();
    }

    /**
     * 校验是否有效
     * @param o Validatable类
     * @return o != null && o.valid()
     */
    static boolean check(Validatable o) {
        return o != null && o.valid();
    }

}
