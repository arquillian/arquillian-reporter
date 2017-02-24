package org.arquillian.reporter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.arquillian.reporter.api.model.AbstractStringKey;
import org.arquillian.reporter.api.model.StringKey;

/**
 * Responsible for loading string-key properties files and setting the values to corresponding classes
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
class StringKeysBuilder {

    private static Logger log = Logger.getLogger(StringKeysBuilder.class.getName());

    static void buildStringKey(StringKey stringKeyClass) {
        String stringKeyClassName = stringKeyClass.getClass().getName().toString();
        String stringKeyFileName = stringKeyClassName + ".properties";
        InputStream stringKeyPropFile = StringKeysBuilder.class.getClassLoader()
            .getResourceAsStream(stringKeyFileName);

        if (stringKeyPropFile != null) {
            try {
                Properties properties = new Properties();
                properties.load(stringKeyPropFile);

                List<Field> fields = SecurityActions.getFields(stringKeyClass.getClass());
                fields.stream().forEach(field -> assignValuesFromProperties(field, stringKeyClass, properties));
            } catch (IOException e) {
                throw new IllegalStateException(
                    "Something bad happened when Arquillian Reporter was reading string-key property file: "
                        + stringKeyFileName, e);
            }
        } else {
            log.warning(
                "There hasn't been found any string-key property file with expected name: " + stringKeyFileName);
        }
    }

    private static void assignValuesFromProperties(Field field, StringKey stringKeyClass, Properties properties) {
        if (AbstractStringKey.class.isAssignableFrom(field.getDeclaringClass())) {
            String name = field.getName();
            try {
                AbstractStringKey key = (AbstractStringKey) field.get(stringKeyClass);
                if (key == null) {
                    key = (AbstractStringKey) SecurityActions
                        .newInstance(stringKeyClass.getClass());
                }
                key.setValue((String) properties.get(name.concat(".value")));
                key.setDescription((String) properties.get(name.concat(".description")));
                key.setIcon((String) properties.get(name.concat(".icon")));

            } catch (IllegalAccessException e) {
                throw new IllegalStateException(String.format(
                    "Something bad happened when Arquillian Reporter was trying to set values of string-keys to the field: %s in the class: %s",
                    field.getName(), stringKeyClass), e);
            }
        }
    }
}
