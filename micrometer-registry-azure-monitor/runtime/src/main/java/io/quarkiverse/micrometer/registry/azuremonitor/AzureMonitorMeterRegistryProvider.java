package io.quarkiverse.micrometer.registry.azuremonitor;

import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.Config;

import io.micrometer.azuremonitor.AzureMonitorConfig;
import io.micrometer.azuremonitor.AzureMonitorMeterRegistry;
import io.micrometer.azuremonitor.AzureMonitorNamingConvention;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.config.MeterRegistryConfigValidator;
import io.micrometer.core.instrument.config.validate.Validated;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.properties.UnlessBuildProperty;
import io.quarkus.micrometer.runtime.export.ConfigAdapter;

@Singleton
public class AzureMonitorMeterRegistryProvider {
    static final String PREFIX = "quarkus.micrometer.export.azuremonitor.";
    static final String PUBLISH = "azuremonitor.publish";
    static final String ENABLED = "azuremonitor.enabled";

    @Produces
    @Singleton
    @DefaultBean
    public AzureMonitorConfig configure(Config config) {
        final Map<String, String> properties = ConfigAdapter.captureProperties(config, PREFIX);

        // Special check: if publish is set, override the value of enabled
        // Specifically, the StatsD registry must be enabled for this
        // Provider to even be present. If this instance (at runtime) wants
        // to prevent metrics from being published, then it would set
        // quarkus.micrometer.export.statsd.publish=false
        if (properties.containsKey(PUBLISH)) {
            properties.put(ENABLED, properties.get(PUBLISH));
        }

        return ConfigAdapter.validate(new AzureMonitorConfig() {
            @Override
            public String get(String key) {
                return properties.get(key);
            }

            @Override
            // Bug in micrometer: instrumentationKey is required
            public Validated<?> validate() {
                return MeterRegistryConfigValidator.checkAll(this,
                        c -> StepRegistryConfig.validate(c),
                        MeterRegistryConfigValidator.checkRequired("instrumentationKey",
                                AzureMonitorConfig::instrumentationKey));
            }
        });
    }

    @Produces
    @DefaultBean
    public AzureMonitorNamingConvention namingConvention() {
        return new AzureMonitorNamingConvention();
    }

    @Produces
    @Singleton
    @UnlessBuildProperty(name = PREFIX + "default-registry", stringValue = "false", enableIfMissing = true)
    public AzureMonitorMeterRegistry registry(AzureMonitorConfig config, Clock clock) {
        return new AzureMonitorMeterRegistry(config, clock);
    }
}
