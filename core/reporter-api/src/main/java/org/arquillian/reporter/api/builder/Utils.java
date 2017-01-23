package org.arquillian.reporter.api.builder;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Utils {

    public static String getDateFormat(){
        return "yyyy-MM-dd HH:mm:ss.S";
    }

    public static String getCurrentDate(){
        return new SimpleDateFormat(getDateFormat()).format(new Date(System.currentTimeMillis()));
    }

    public static String getTestMethodId(Method method){
        if (method != null) {
            return buildId(method.getDeclaringClass().toString(), method.getName());
        }
        return null;
    }

    public static String buildId(String... sectionIdParams) {
        StringBuffer id = new StringBuffer();
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
        return id.toString();
    }

    public static String humanReadableByteCount(Long bytes, boolean decimal) {
        if (bytes == null) bytes = 0L;
        String sign = bytes < 0 ? "-" : "";
        Long absBytes = Math.abs(bytes);
        int unit = decimal ? 1000 : 1024;
        if (absBytes < unit) {
            return sign + absBytes + " B";
        }
        int exp = (int) (Math.log(absBytes) / Math.log(unit));
        String pre = (decimal ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (decimal ? "" : "i");

        return  String.format("%s %.2f %sB", sign, absBytes / Math.pow(unit, exp), pre).trim();
    }

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
