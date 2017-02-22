package org.arquillian.reporter.api.utils;

/**
 * A validation class
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Validate {

    /**
     * Checks if the given string is null or empty
     *
     * @param string String to check
     * @return If the given string is null or empty
     */
    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * Checks if the given string is not null nor empty
     *
     * @param string String to check
     * @return If the given string is not null nor empty
     */
    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }
}
