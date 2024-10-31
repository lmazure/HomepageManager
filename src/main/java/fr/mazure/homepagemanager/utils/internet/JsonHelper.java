package fr.mazure.homepagemanager.utils.internet;

import org.json.JSONArray;
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

    /**
     * Return the value of a JSON Object field of field of ...
     *
     * @param node Starting node
     * @param fields Hierarchy of fields
     * @return The field value
     */
    public static JSONObject getAsNode(final JSONObject node,
                                       final String... fields) {
        final Object n = getAsObject(node, fields);
        if (!(n instanceof JSONObject)) {
            throw new IllegalStateException("Field '" + fields[fields.length - 1] + "' in '" + node.toString() + "' is not a JSONObject.");
        }
        return (JSONObject)n;
    }

    /**
     * Return the value of a JSON Array field of field of ...
     *
     * @param node Starting node
     * @param fields Hierarchy of fields
     * @return The field value
     */
    public static JSONArray getAsArray(final JSONObject node,
                                       final String... fields) {
        final Object n = getAsObject(node, fields);
        if (!(n instanceof JSONArray)) {
            throw new IllegalStateException("Field '" + fields[fields.length - 1] + "' in '" + node.toString() + "' is not a JSONArray.");
        }
        return (JSONArray)n;
    }

    /**
     * Return the value of a text field of a field of ...
     *
     * @param node Starting node
     * @param fields Hierarchy of fields
     * @return The field value
     */
    public static String getAsText(final JSONObject node,
                                   final String... fields) {
        final Object n = getAsObject(node, fields);
        if (!(n instanceof String)) {
            throw new IllegalStateException("Field '" + fields[fields.length - 1] + "' in '" + node.toString() + "' is not a String.");
        }
        return (String)n;
    }

    /**
     * Return the value of an integer field of a field of ...
     *
     * @param node Starting node
     * @param fields Hierarchy of fields
     * @return The field value
     */
    public static int getAsInt(final JSONObject node,
                               final String... fields) {
        final Object n = getAsObject(node, fields);
        if (!(n instanceof Integer)) {
            throw new IllegalStateException("Field '" + fields[fields.length - 1] + "' in '" + node.toString() + "' is not a Integer.");
        }
        return ((Integer)n).intValue();
    }

    /**
     * Return the value of an integer text field of a field of ...
     *
     * @param node Starting node
     * @param fields Hierarchy of fields
     * @return The field value
     */
    public static int getAsTextInt(final JSONObject node,
                                   final String... fields) {
        final String str = getAsText(node, fields);
        try {
            return Integer.parseInt(str);
        } catch (@SuppressWarnings("unused") final NumberFormatException e) {
            throw new IllegalStateException("Field '" + fields[fields.length - 1] + "' in '" + node.toString() + "' is not an Integer String.");
        }
    }

    private static Object getAsObject(final JSONObject node,
                                      final String... fields) {
        if (fields.length == 0) {
            return node;
        }
        JSONObject currentNode = node;
        for (int i = 0; i < (fields.length -1); i++) {
            final Object n = currentNode.get(fields[i]);
            if (n == null) {
                throw new IllegalStateException("Cannot find field '" + fields[i] + "' in '" + currentNode.toString() + "'.");
            }
            if (!(n instanceof JSONObject)) {
                throw new IllegalStateException("Field '" + fields[i] + "' in '" + currentNode.toString() + "' is not a node.");
            }
            currentNode = (JSONObject) n;
        }
        return currentNode.get(fields[fields.length - 1]);
    }
}