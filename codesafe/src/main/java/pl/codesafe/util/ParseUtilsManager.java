package pl.codesafe.util;

import java.util.function.BiFunction;
import javax.annotation.Nonnull;

/**
 * 管理ParseUtils
 * @author LiYan
 */
public class ParseUtilsManager {

    private static BiFunction<String, Class<?>, ?> jsonObjectParser;

    private static BiFunction<String, Class<?>, ?> jsonArrayParser;

    public static void setJsonObjectParser(@Nonnull BiFunction<String, Class<?>, ?> jsonObjectParser) {
        if (ParseUtilsManager.jsonObjectParser == null) {
            throw new RuntimeException("jsonParser of ParseUtilsManager can only set once");
        }
        ParseUtilsManager.jsonObjectParser = jsonObjectParser;
    }

    public static void setJsonArrayParser(@Nonnull BiFunction<String, Class<?>, ?> jsonArrayParser) {
        if (ParseUtilsManager.jsonArrayParser == null) {
            throw new RuntimeException("jsonParser of ParseUtilsManager can only set once");
        }
        ParseUtilsManager.jsonArrayParser = jsonArrayParser;
    }

    public static BiFunction<String, Class<?>, ?> getJsonObjectParser() {
        if (jsonObjectParser == null) {
            throw new RuntimeException("jsonObjectParser of ParseUtilsManager had not set");
        }
        return jsonObjectParser;
    }

    public static BiFunction<String, Class<?>, ?> getJsonArrayParser() {
        if (jsonArrayParser == null) {
            throw new RuntimeException("jsonArrayParser of ParseUtilsManager had not set");
        }
        return jsonArrayParser;
    }
}
