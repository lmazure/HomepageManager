package fr.mazure.homepagemanager.utils.internet;

import org.json.JSONObject;

/**
 * Methods to manage JSON
 */
public class JsonHelper {

    /**
     * Unescape a JSON string
     *
     * @param str string to unescape
     * @return the unescaped string
     */
    public static String unescape(final String str) {
        final String jsonString = "{\"key\":\"" + str + "\"}";
        final JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.getString("key");
    }
}