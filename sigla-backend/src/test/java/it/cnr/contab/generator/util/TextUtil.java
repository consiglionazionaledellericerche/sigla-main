package it.cnr.contab.generator.util;

import java.text.MessageFormat;
import java.util.StringTokenizer;

/**
 * Helper class to format message strings.
 *
 * @author Marco Spasiano
 * @version 1.0 [17-Aug-2006]
 */
public class TextUtil {
    private TextUtil() {
        // Not for instantiation
    }

    public static String format(String message, Object object) {
        return MessageFormat.format(message, new Object[]{object});
    }

    public static String format(String message, Object[] objects) {
        return MessageFormat.format(message, objects);
    }

    public static String capitalize(String source) {
        source = source.substring(0, 1).toUpperCase() + source.substring(1);
        return source;
    }

    public static String capitalizeAndLower(String source) {
        source = source.toLowerCase();
        source = source.substring(0, 1).toUpperCase() + source.substring(1);
        return source;
    }

    public static String lowerFirst(String source) {
        source = source.substring(0, 1).toLowerCase() + source.substring(1);
        return source;
    }

    public static String removeSpaces(String s) {
        StringTokenizer st = new StringTokenizer(s, " ", false);
        String t = "";
        while (st.hasMoreElements()) t += st.nextElement();
        return t;
    }

    public static String nvl(String s) {
        if (s == null)
            return " ";
        return s;
    }

    public static String capitalizeUnderscore(String attributeName) {
        attributeName = attributeName.toLowerCase();
        StringBuffer result = new StringBuffer();
        StringTokenizer st = new StringTokenizer(attributeName, "_", false);
        while (st.hasMoreTokens()) {
            String part = st.nextToken();
            result.append(capitalize(part));
        }
        return result.toString();
    }
}