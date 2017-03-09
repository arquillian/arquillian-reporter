package org.arquillian.reporter.api.builder;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

import org.arquillian.reporter.api.builder.entry.DataCollectionBuilder;
import org.arquillian.reporter.api.builder.entry.TableBuilder;
import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.table.TableEntry;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.Report;

/**
 * The main starting point for using Arquillian Reporter fluent API
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Reporter {

    private static final Logger log = Logger.getLogger(Reporter.class.getName());

    /**
     * Creates an instance of {@link BasicReport} and sets it into a {@link ReportBuilder} as a report to be built.
     * The {@link ReportBuilder} instance will be then returned.
     *
     * @param name A StringKey representing a name of the {@link BasicReport} instance
     * @return An instance of {@link ReportBuilder} with set instance of {@link BasicReport}
     */
    public static ReportBuilder createReport(StringKey name) {
        return usingBuilder(ReportBuilder.class, new BasicReport(name));
    }

    /**
     * Creates an instance of {@link BasicReport} without any name specified. It sets the BasicReport implementation
     * into a {@link ReportBuilder} as a report to be built. The {@link ReportBuilder} instance will be then returned.
     *
     * @return An instance of {@link ReportBuilder} with set instance of {@link BasicReport}
     */
    public static ReportBuilder createReport() {
        return usingBuilder(ReportBuilder.class, new BasicReport(""));
    }

    /**
     * Creates an instance of {@link BasicReport} and sets it into a {@link ReportBuilder} as a report to be built.
     * The {@link ReportBuilder} instance will be then returned.
     *
     * @param name A name of the {@link BasicReport} instance. The name will be stored as an {@link UnknownStringKey}
     * @return An instance of {@link ReportBuilder} with set instance of {@link BasicReport}
     */
    public static ReportBuilder createReport(String name) {
        return usingBuilder(ReportBuilder.class, new BasicReport(name));
    }

    /**
     * Uses the given instance of {@link Report} and creates corresponding instance of {@link ReportBuilder} that should
     * be used for the given type of {@link Report}.
     * The {@link ReportBuilder} instance will be then returned.
     *
     * @param report An instance of {@link Report} implementation to be built
     * @return An instance of {@link ReportBuilder} with the given report set
     */
    public static <BUILDERTYPE extends ReportBuilder<BUILDERTYPE, ? extends Report>, REPORTTYPE extends Report<? extends Report, BUILDERTYPE>> BUILDERTYPE createReport(
        REPORTTYPE report) {
        return usingBuilder(report.getReportBuilderClass(), report);
    }

    /**
     * Creates an instance of the given {@link Builder} using the given constructor parameters. The instance of the builder is then returned.
     *
     * @param builderClass    An implementation class of the interface {@link Builder} to be created
     * @param constructParams Constructor parameters that should be used for instantiating of the given builder class
     * @param <BUILDERTYPE>   The type of the given builder class
     * @return An instance of given {@link Builder} class
     */
    public static <BUILDERTYPE extends Builder> BUILDERTYPE usingBuilder(Class<BUILDERTYPE> builderClass,
        Object... constructParams) {
        Class<BUILDERTYPE> implClass = BuilderRegistry.getImplementationForBuilder(builderClass);

        if (implClass != null) {
            Class<?>[] classes = getConstructorParametersTypes(implClass, constructParams);
            return newInstance(implClass, classes, constructParams);
        } else {
            throw new IllegalArgumentException(
                "There is no implementation registered for the builder: " + builderClass.getCanonicalName());
        }
    }

    /**
     * Creates an instance of {@link TableEntry} class and sets it into a {@link TableBuilder} as an entry to be built.
     * The {@link TableBuilder} instance will be then returned.
     *
     * @param name A name of the {@link TableEntry} instance. The name will be stored as an {@link UnknownStringKey}
     * @return An instance of {@link TableBuilder} with the given table entry set
     */
    public static TableBuilder createTable(String name) {
        return usingBuilder(TableBuilder.class, name);
    }

    /**
     * Creates an instance of {@link TableEntry} class and sets it into a {@link TableBuilder} as an entry to be built.
     * The {@link TableBuilder} instance will be then returned.
     *
     * @param name A StringKey representing name of the {@link TableEntry} instance.
     * @return An instance of {@link TableBuilder} with the given table entry set
     */
    public static TableBuilder createTable(StringKey name) {
        return usingBuilder(TableBuilder.class, name);
    }

    public static DataCollectionBuilder createDataCollection(String name) {
        return usingBuilder(DataCollectionBuilder.class, name);
    }

    public static DataCollectionBuilder createDataCollection(StringKey name) {
        return usingBuilder(DataCollectionBuilder.class, name);
    }

    // security actions

    /**
     * Gets an array of constructor parameters types of the given class, based on the given object parameters
     *
     * @param implClass       A class the constructor should be get for
     * @param constructParams List of parameters that the constructor should match
     * @return An array of constructor parameters types of the given class, that should match the given object parameters
     */
    private static Class<?>[] getConstructorParametersTypes(Class<?> implClass, Object... constructParams) {
        Constructor<?>[] constructors = implClass.getConstructors();

        if (constructors.length == 1) {
            return constructors[0].getParameterTypes();
        } else {
            Class<?>[] expectedClasses =
                Arrays.stream(constructParams).map(param -> param.getClass()).toArray(Class<?>[]::new);
            Optional<Constructor<?>> firstMatchedConstr = getFirstMatchedConstructor(constructors, expectedClasses);
            if (firstMatchedConstr.isPresent()) {
                return firstMatchedConstr.get().getParameterTypes();
            } else {
                log.warning(String.format(
                    "It wasn't possible to find any constructor method of the class %s with expected set of constructor parameter types: %s",
                    implClass, Arrays.asList(constructParams)));
                return expectedClasses;
            }
        }
    }

    /**
     * Gets first constructor that matches the given expected classes
     *
     * @param constructors    An array of constructors to be searched
     * @param expectedClasses An array of expected classes the constructor should match
     * @return First constructor that matches the given expected classes
     */
    private static Optional<Constructor<?>> getFirstMatchedConstructor(Constructor<?>[] constructors,
        Class<?>[] expectedClasses) {
        return Arrays
            .stream(constructors)
            .filter(constr -> {
                if (constr.getParameterCount() != expectedClasses.length) {
                    return false;
                }
                Class<?>[] actualParams = constr.getParameterTypes();
                for (int i = 0; i < actualParams.length; i++) {
                    if (!actualParams[i].isAssignableFrom(expectedClasses[i])) {
                        return false;
                    }
                }
                return true;
            })
            .findFirst();

    }

    /**
     * Create a new instance by finding a constructor that matches the argumentTypes signature
     * using the arguments for instantiation.
     *
     * @param implClass     Full classname of class to create
     * @param argumentTypes The constructor argument types
     * @param arguments     The constructor arguments
     * @return a new instance
     * @throws IllegalArgumentException if className, argumentTypes, or arguments are null
     * @throws RuntimeException         if any exceptions during creation
     * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
     * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
     */
    private static <T> T newInstance(final Class<T> implClass, final Class<?>[] argumentTypes,
        final Object[] arguments) {
        if (implClass == null) {
            throw new IllegalArgumentException("ImplClass must be specified");
        }
        if (argumentTypes == null) {
            throw new IllegalArgumentException("ArgumentTypes must be specified. Use empty array if no arguments");
        }
        if (arguments == null) {
            throw new IllegalArgumentException("Arguments must be specified. Use empty array if no arguments");
        }
        final T obj;
        try {
            final Constructor<T> constructor = getConstructor(implClass, argumentTypes);
            if (!constructor.isAccessible()) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    public Void run() {
                        constructor.setAccessible(true);
                        return null;
                    }
                });
            }
            obj = constructor.newInstance(arguments);
        } catch (Exception e) {
            throw new RuntimeException("Could not create new instance of " + implClass, e);
        }

        return obj;
    }

    /**
     * Obtains the Constructor specified from the given Class and argument types
     *
     * @param clazz
     * @param argumentTypes
     * @return
     * @throws NoSuchMethodException
     */
    private static <T> Constructor<T> getConstructor(final Class<T> clazz, final Class<?>... argumentTypes)
        throws NoSuchMethodException {
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor<T>>() {
                public Constructor<T> run() throws NoSuchMethodException {
                    return clazz.getDeclaredConstructor(argumentTypes);
                }
            });
        }
        // Unwrap
        catch (final PrivilegedActionException pae) {
            final Throwable t = pae.getCause();
            // Rethrow
            if (t instanceof NoSuchMethodException) {
                throw (NoSuchMethodException) t;
            } else {
                // No other checked Exception thrown by Class.getConstructor
                try {
                    throw (RuntimeException) t;
                }
                // Just in case we've really messed up
                catch (final ClassCastException cce) {
                    throw new RuntimeException("Obtained unchecked Exception; this code should never be reached", t);
                }
            }
        }
    }
}
