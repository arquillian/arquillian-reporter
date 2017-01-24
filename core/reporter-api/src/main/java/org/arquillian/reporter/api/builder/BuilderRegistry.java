package org.arquillian.reporter.api.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
class BuilderRegistry {

    private static Map<String, String> builderRegistry = null;
    private static final String BUILDER_IMPLEMENTATION_MAPPER_FILE =
        "META-INF/services/org.arquillian.reporter.Builder";

    private BuilderRegistry() {

    }

    static synchronized String getImplementationForBuilder(String builder) {
        if (builderRegistry == null) {
            loadFromFile();
        }
        return builderRegistry.get(builder);
    }

    private static void loadFromFile() {
        builderRegistry = new HashMap<>();
        InputStream builderImplMap =
            BuilderRegistry.class.getClassLoader().getResourceAsStream(BUILDER_IMPLEMENTATION_MAPPER_FILE);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(builderImplMap))) {
            buffer.lines().forEach(line -> {
                String[] interfaceAndImpl = line.split("=");
                if (interfaceAndImpl.length == 2) {
                    builderRegistry.put(interfaceAndImpl[0].trim(), interfaceAndImpl[1].trim());
                } else {
                    // todo
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            // todo
        }
    }
}
