package org.arquillian.reporter.api.utils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Util class
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReporterUtils {

    /**
     * Returns the date format that should be used for dates
     *
     * @return The date format that should be used for dates
     */
    public static String getDateFormat() {
        return "yyyy-MM-dd HH:mm:ss.S";
    }

    /**
     * Returns the current date in the format that matches {@link this#getDateFormat()}
     *
     * @return The current date in the format that matches {@link this#getDateFormat()}
     */
    public static String getCurrentDate() {
        return new SimpleDateFormat(getDateFormat()).format(new Date(System.currentTimeMillis()));
    }

    /**
     * Takes the given {@link Method}, retrieves the declaring class and creates an id representing the method using this format:
     * declaring.class.Name#testMethodName
     *
     * @param method A {@link Method} to be used for creating an id
     * @return Created an id of the given {@link Method}
     */
    public static String getTestMethodId(Method method) {
        if (method != null) {
            return buildId(method.getDeclaringClass().toString(), method.getName());
        }
        return null;
    }

    /**
     * Takes the given sectionIdParams and using them creates a String identifier.
     * The identifier is created using all sectionIdParams joining them together using a char: '#'
     * <p>
     *     The resulting identifier of two sectionIdParams 'bar' and 'foo' is: 'bar#foo'
     * </p>
     *
     * @param sectionIdParams Strings to be used for creating identifier
     */
    public static String buildId(String... sectionIdParams) {
        StringBuffer id = new StringBuffer();
        if (sectionIdParams != null) {
            for (String sectionIdParam : sectionIdParams) {
                if (sectionIdParam != null) {
                    if (!id.toString().isEmpty()) {
                        id.append("#");
                    }
                    id.append(sectionIdParam);
                } else {
                    return null;
                }
            }
        }
        return id.toString();
    }

    /**
     * Takes the given bytes and transforms them into a human readable format
     *
     * @param bytes Bytes to be transformed
     * @param decimal If the number is in decimal
     * @return The transformed bytes in human readable format
     */
    public static String humanReadableByteCount(Long bytes, boolean decimal) {
        if (bytes == null)
            bytes = 0L;
        String sign = bytes < 0 ? "-" : "";
        Long absBytes = Math.abs(bytes);
        int unit = decimal ? 1000 : 1024;
        if (absBytes < unit) {
            return sign + absBytes + " B";
        }
        int exp = (int) (Math.log(absBytes) / Math.log(unit));
        String pre = (decimal ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (decimal ? "" : "i");

        return String.format("%s %.2f %sB", sign, absBytes / Math.pow(unit, exp), pre).trim();
    }

    /**
     * Convert any number to long
     *
     * @param number A number to convert
     * @return Converted number to long
     */
    public static long convertNumberToLong(Object number) {
        long longNumber = 0;
        if (number != null) {
            NumberType type = NumberType.valueOf(number.getClass().getSimpleName().toUpperCase());

            switch (type) {
                case BYTE:
                    longNumber = ((Byte) number).longValue();
                    break;
                case SHORT:
                    longNumber = ((Short) number).longValue();
                    break;
                case INTEGER:
                    longNumber = ((Integer) number).longValue();
                    break;
                case LONG:
                    longNumber = (long) number;
                    break;
            }
        }
        return longNumber;
    }

    enum NumberType {
        BYTE,
        SHORT,
        INTEGER,
        LONG;
    }
}
