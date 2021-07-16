package io.quarkiverse.micrometer.registry.azuremonitor;

import java.util.Map;
import java.util.Optional;

import io.quarkus.micrometer.runtime.config.MicrometerConfig;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

public class AzureMonitorConfig {

    @ConfigRoot(name = "micrometer.export.azuremonitor", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
    public static class AzureMonitorBuildConfig implements MicrometerConfig.CapabilityEnabled {
        /**
         * Support for export to AzureMonitor.
         * <p>
         * Support for AzureMonitor will be enabled if Micrometer
         * support is enabled, the Azure Monitor registry extension is enabled
         * and either this value is true, or this value is unset and
         * {@code quarkus.micrometer.registry-enabled-default} is true.
         */
        @ConfigItem
        public Optional<Boolean> enabled;

        @Override
        public Optional<Boolean> getEnabled() {
            return enabled;
        }

        /**
         * By default, this extension will create a AzureMonitor MeterRegistry instance.
         * <p>
         * Use this attribute to veto the creation of the default AzureMonitor MeterRegistry.
         */
        @ConfigItem(defaultValue = "true")
        public boolean defaultRegistry;

        @Override
        public String toString() {
            return this.getClass().getSimpleName()
                    + "{enabled=" + enabled
                    + ",defaultRegistry=" + defaultRegistry
                    + '}';
        }
    }

    /**
     * Runtime configuration for Azure Monitor MeterRegistry
     */
    @ConfigRoot(name = "micrometer.export.azuremonitor", phase = ConfigPhase.RUN_TIME)
    public static class AzureMonitorRuntimeConfig {
        // @formatter:off
        /**
         * Azure Monitor registry configuration properties.
         *
         * A property source for configuration of the AzureMonitor MeterRegistry.
         *
         * Available values:
         *
         * [cols=2]
         * !===
         * h!Property=Default
         * h!Description
         *
         * !`instrumentation-key`
         * !Define the instrumentation key used to push data to Azure Insights Monitor
         *
         * !`publish=true`
         * !By default, gathered metrics will be published to Azure Monitor when the MeterRegistry is enabled.
         * Use this attribute to selectively disable publication of metrics in some environments.
         *
         * !===
         *
         * Other Micrometer configuration attributes can also be specified.
         *
         * @asciidoclet
         */
        // @formatter:on
        @ConfigItem(name = ConfigItem.PARENT)
        Map<String, String> azuremonitor;
    }
}
