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
}
