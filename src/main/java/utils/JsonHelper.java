package utils;

public class JsonHelper {

    public static String unescapeString(final String str) {
        return str.replace("\\n", "\n")
                  .replace("\\u002F", "/")
                  .replace("\\u003E", ">");
    }
}
