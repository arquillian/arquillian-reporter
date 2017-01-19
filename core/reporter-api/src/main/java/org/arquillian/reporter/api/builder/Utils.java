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
        return String.format("%s#%s", method.getDeclaringClass(), method.getName());
    }
}
