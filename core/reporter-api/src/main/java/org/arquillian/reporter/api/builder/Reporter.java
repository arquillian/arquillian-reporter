package org.arquillian.reporter.api.builder;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Reporter {

    public static ReportBuilder createReport(StringKey name){
        return usingBuilder(ReportBuilder.class, new Report(name));
    }

    public static <T extends ReportBuilder<? extends AbstractReport,T>, S extends AbstractReport<? extends AbstractReport, T>> T createReport(S report) {
        return usingBuilder(report.getReportBuilderClass(), report);
    }

    public static <T extends Builder> T usingBuilder(Class<T> builderClass, Object... constructParams){
        String implementationForBuilder = BuilderRegistry.getImplementationForBuilder(builderClass.getCanonicalName());
        if (implementationForBuilder != null) {
            Class<?>[] classes = Arrays.stream(constructParams).map(param -> param.getClass()).toArray(Class<?>[]::new);
            Class<T> implClass = (Class<T>) loadClass(implementationForBuilder);
            Constructor<?>[] constructors = implClass.getConstructors();
            if (constructors.length == 1){
                classes = constructors[0].getParameterTypes();
            }

            return newInstance(implementationForBuilder, classes, constructParams, builderClass);
        } else {
            // todo
            throw  new IllegalArgumentException("There is no implementation registered for the builder: " + builderClass.getCanonicalName());
        }
    }

//    public static FireReport reportSection(Report sectionReport){
//        return new FireReportImpl(sectionReport);
//    }

    public static TableBuilder createTable(String name) {
        return usingBuilder(TableBuilder.class, name);
    }

    public static TableBuilder createTable(StringKey name) {
        return usingBuilder(TableBuilder.class, name);
    }

//    public static CreateNode createNode(ReportNodeEvent reportNodeEvent){
//        return new CreateNode()
//    }

    /**
     * Obtains the Thread Context ClassLoader
     */
    static ClassLoader getThreadContextClassLoader()
    {
        return AccessController.doPrivileged(GetTcclAction.INSTANCE);
    }

    static boolean isClassPresent(String name)
    {
        try
        {
            loadClass(name);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    static Class<?> loadClass(String className)
    {
        try
        {
            return Class.forName(className, true, getThreadContextClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            try
            {
                return Class.forName(className, true, Reporter.class.getClassLoader());
            }
            catch (ClassNotFoundException e2)
            {
                throw new RuntimeException("Could not load class " + className, e2);
            }
        }
    }

    static <T> T newInstance(final String className, final Class<?>[] argumentTypes, final Object[] arguments, final Class<T> expectedType)
    {
        @SuppressWarnings("unchecked")
        Class<T> implClass = (Class<T>) loadClass(className);
        if (!expectedType.isAssignableFrom(implClass)) {
            throw new RuntimeException("Loaded class " + className + " is not of expected type " + expectedType);
        }
        return newInstance(implClass, argumentTypes, arguments);
    }

    static <T> T newInstance(final String className, final Class<?>[] argumentTypes, final Object[] arguments, final Class<T> expectedType, ClassLoader classLoader)
    {
        Class<?> clazz = null;
        try
        {
            clazz = Class.forName(className, false, classLoader);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not load class " + className, e);
        }
        Object obj = newInstance(clazz, argumentTypes, arguments);
        try
        {
            return expectedType.cast(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Loaded class " + className + " is not of expected type " + expectedType, e);
        }
    }

    /**
     * Create a new instance by finding a constructor that matches the argumentTypes signature
     * using the arguments for instantiation.
     *
     * @param implClass Full classname of class to create
     * @param argumentTypes The constructor argument types
     * @param arguments The constructor arguments
     * @return a new instance
     * @throws IllegalArgumentException if className, argumentTypes, or arguments are null
     * @throws RuntimeException if any exceptions during creation
     * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
     * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
     */
    static <T> T newInstance(final Class<T> implClass, final Class<?>[] argumentTypes, final Object[] arguments)
    {
        if (implClass == null)
        {
            throw new IllegalArgumentException("ImplClass must be specified");
        }
        if (argumentTypes == null)
        {
            throw new IllegalArgumentException("ArgumentTypes must be specified. Use empty array if no arguments");
        }
        if (arguments == null)
        {
            throw new IllegalArgumentException("Arguments must be specified. Use empty array if no arguments");
        }
        final T obj;
        try
        {
            final Constructor<T> constructor = getConstructor(implClass, argumentTypes);
            if(!constructor.isAccessible()) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    public Void run() {
                        constructor.setAccessible(true);
                        return null;
                    }
                });
            }
            obj = constructor.newInstance(arguments);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not create new instance of " + implClass, e);
        }

        return obj;
    }

    /**
     * Obtains the Constructor specified from the given Class and argument types
     * @param clazz
     * @param argumentTypes
     * @return
     * @throws NoSuchMethodException
     */
    static <T> Constructor<T> getConstructor(final Class<T> clazz, final Class<?>... argumentTypes)
        throws NoSuchMethodException
    {
        try
        {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor<T>>()
            {
                public Constructor<T> run() throws NoSuchMethodException
                {
                    return clazz.getDeclaredConstructor(argumentTypes);
                }
            });
        }
        // Unwrap
        catch (final PrivilegedActionException pae)
        {
            final Throwable t = pae.getCause();
            // Rethrow
            if (t instanceof NoSuchMethodException)
            {
                throw (NoSuchMethodException) t;
            }
            else
            {
                // No other checked Exception thrown by Class.getConstructor
                try
                {
                    throw (RuntimeException) t;
                }
                // Just in case we've really messed up
                catch (final ClassCastException cce)
                {
                    throw new RuntimeException("Obtained unchecked Exception; this code should never be reached", t);
                }
            }
        }
    }

    /**
     * Single instance to get the TCCL
     */
    private enum GetTcclAction implements PrivilegedAction<ClassLoader> {
        INSTANCE;

        public ClassLoader run()
        {
            return Thread.currentThread().getContextClassLoader();
        }

    }
}
