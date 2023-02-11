package fr.mazure.homepagemanager.utils;

/**
 * Tools to manage Json
 */
public class JsonHelper {

    /**
     * Unescape a Json string (i.e. convert a Json string into a string)
     *
     * @param str Json string
     * @return string
     */
    public static String unescapeString(final String str) {
        return str.replace("\\n", "\n")
                  .replace("\\u002F", "/")
                  .replace("\\u003E", ">");
    }
}
