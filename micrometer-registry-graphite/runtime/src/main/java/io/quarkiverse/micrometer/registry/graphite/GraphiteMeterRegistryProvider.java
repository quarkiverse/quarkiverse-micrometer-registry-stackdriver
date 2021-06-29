package io.quarkiverse.micrometer.registry.graphite;

import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.Config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import io.micrometer.graphite.GraphiteConfig;
import io.micrometer.graphite.GraphiteDimensionalNameMapper;
import io.micrometer.graphite.GraphiteHierarchicalNameMapper;
import io.micrometer.graphite.GraphiteMeterRegistry;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.properties.UnlessBuildProperty;
import io.quarkus.micrometer.runtime.export.ConfigAdapter;

@Singleton
public class GraphiteMeterRegistryProvider {
    static final String PREFIX = "quarkus.micrometer.export.graphite.";

    @Produces
    @Singleton
    @DefaultBean
    public GraphiteConfig configure(Config config) {
        final Map<String, String> properties = ConfigAdapter.captureProperties(config, PREFIX);
        return ConfigAdapter.validate(properties::get);
    }

    @Produces
    @Singleton
    @DefaultBean
    @GraphiteNameMapper
    public HierarchicalNameMapper nameMapper(GraphiteConfig config) {
        return config.graphiteTagsEnabled() ? new GraphiteDimensionalNameMapper()
                : new GraphiteHierarchicalNameMapper(config.tagsAsPrefix());
    }

    @Produces
    @Singleton
    @UnlessBuildProperty(name = PREFIX + "default-registry", stringValue = "false", enableIfMissing = true)
    public GraphiteMeterRegistry registry(GraphiteConfig config,
            @GraphiteNameMapper HierarchicalNameMapper nameMapper,
            Clock clock) {
        return new GraphiteMeterRegistry(config, clock, nameMapper);
    }
}
