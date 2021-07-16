package io.quarkiverse.micrometer.registry.signalfx;

import java.util.Map;
import java.util.Optional;

import io.quarkus.micrometer.runtime.config.MicrometerConfig;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

public class SignalFxConfig {

    @ConfigRoot(name = "micrometer.export.signalfx", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
    public static class SignalFxBuildConfig implements MicrometerConfig.CapabilityEnabled {
        /**
         * Support for export to SignalFx.
         * <p>
         * Support for SignalFx will be enabled if Micrometer
         * support is enabled, the SignalFx registry extension is enabled
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
         * By default, this extension will create a SignalFx MeterRegistry instance.
         * <p>
         * Use this attribute to veto the creation of the default SignalFx MeterRegistry.
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
     * Runtime configuration for SignalFX MeterRegistry
     */
    @ConfigRoot(name = "micrometer.export.signalfx", phase = ConfigPhase.RUN_TIME)
    public static class SignalFxRuntimeConfig {
        // @formatter:off
        /**
         * SignalFx registry configuration properties.
         *
         * A property source for configuration of the SignalFx MeterRegistry,
         * see https://micrometer.io/docs/registry/signalFx.
         *
         * Available values:
         *
         * [cols=2]
         * !===
         * h!Property=Default
         * h!Description
         *
         * !`access-token=MY_ACCESS_TOKEN`
         * !Define the access token required to push data to SignalFx
         *
         * !`source=identifier`
         * !Unique identifier for the app instance that is publishing metrics to SignalFx.
         * Defaults to the local host name.
         *
         * !`uri=https://ingest.signalfx.com`
         * !Define the the URI to ship metrics to. Use this attribute to specify
         * the location of an internal proxy, if necessary.
         *
         * !`publish=true`
         * !By default, gathered metrics will be published to SignalFx when the MeterRegistry is enabled.
         * Use this attribute to selectively disable publication of metrics in some environments.
         *
         * !`step=1m`
         * !The interval at which metrics are sent to SignalFx Monitoring. The default is 1 minute.
         * !===
         *
         * Other Micrometer configuration attributes can also be specified.
         *
         * @asciidoclet
         */
        // @formatter:on
        @ConfigItem(name = ConfigItem.PARENT)
        Map<String, String> signalfx;
    }
}
