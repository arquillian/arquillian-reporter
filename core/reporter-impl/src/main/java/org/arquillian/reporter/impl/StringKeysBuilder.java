package org.arquillian.reporter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import org.arquillian.reporter.api.model.AbstractStringKey;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
class StringKeysBuilder {

    static void buildStringKey(StringKey stringKeyClass) {
        String stringKeyClassName = stringKeyClass.getClass().getName().toString();
        InputStream stringKeyPropFile = StringKeysBuilder.class.getClassLoader()
            .getResourceAsStream("META-INF/services/" + stringKeyClassName + ".properties");

        if (stringKeyPropFile != null)
            try {
                Properties properties = new Properties();
                properties.load(stringKeyPropFile);

                List<Field> fields = SecurityActions.getFields(stringKeyClass.getClass());
                fields.stream().forEach(field -> assignValuesFromProperties(field, stringKeyClass, properties));
            } catch (IOException e) {
                e.printStackTrace();
                // todo
            }
        else {
            // todo
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
                e.printStackTrace();
                // todo
            }
        }
    }
}
