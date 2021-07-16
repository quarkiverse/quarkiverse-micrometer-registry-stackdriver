package io.quarkiverse.micrometer.registry.signalfx;

import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.Config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.signalfx.SignalFxConfig;
import io.micrometer.signalfx.SignalFxMeterRegistry;
import io.micrometer.signalfx.SignalFxNamingConvention;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.properties.UnlessBuildProperty;
import io.quarkus.micrometer.runtime.export.ConfigAdapter;

@Singleton
public class SignalFxMeterRegistryProvider {
    static final String PREFIX = "quarkus.micrometer.export.signalfx.";
    static final String PUBLISH = "signalfx.publish";
    static final String ENABLED = "signalfx.enabled";

    @Produces
    @Singleton
    @DefaultBean
    public SignalFxConfig configure(Config config) {
        final Map<String, String> properties = ConfigAdapter.captureProperties(config, PREFIX);

        return ConfigAdapter.validate(properties::get);
    }

    @Produces
    @DefaultBean
    public SignalFxNamingConvention namingConvention() {
        return new SignalFxNamingConvention();
    }

    @Produces
    @Singleton
    @UnlessBuildProperty(name = PREFIX + "default-registry", stringValue = "false", enableIfMissing = true)
    public SignalFxMeterRegistry registry(SignalFxConfig config, Clock clock) {
        return new SignalFxMeterRegistry(config, clock);
    }
}
