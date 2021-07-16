package io.quarkiverse.micrometer.registry.stackdriver;

import java.util.Map;
import java.util.Optional;

import io.quarkus.micrometer.runtime.config.MicrometerConfig;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

public class StackdriverConfig {

    @ConfigRoot(name = "micrometer.export.stackdriver", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
    public static class StackdriverBuildConfig implements MicrometerConfig.CapabilityEnabled {
        /**
         * Support for export to Stackdriver.
         * <p>
         * Support for Stackdriver will be enabled if Micrometer
         * support is enabled, the Stackdriver registry extension is enabled
         * and either this value is true, or this value is unset and
         * `quarkus.micrometer.registry-enabled-default` is true.
         * <p>
         * [NOTE]
         * ====
         * Stackdriver libraries do not yet support running in native mode.
         * The Stackdriver MeterRegistry will be automatically disabled
         * for native builds.
         * <p>
         * See https://github.com/grpc/grpc-java/issues/5460
         * ====
         *
         * @asciidoclet
         */
        @ConfigItem
        public Optional<Boolean> enabled;

        @Override
        public Optional<Boolean> getEnabled() {
            return enabled;
        }

        /**
         * By default, this extension will create a Stackdriver MeterRegistry instance.
         * <p>
         * Use this attribute to veto the creation of the default Stackdriver MeterRegistry.
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

    @ConfigRoot(name = "micrometer.export.stackdriver", phase = ConfigPhase.RUN_TIME)
    public static class StackdriverRuntimeConfig {
        // @formatter:off
        /**
         * Stackdriver registry configuration properties.
         *
         * A property source for configuration of the Stackdriver MeterRegistry,
         * see https://micrometer.io/docs/registry/stackdriver.
         *
         * Available values:
         *
         * [cols=2]
         * !===
         * h!Property=Default
         * h!Description
         *
         * !`project-id=MY_PROJECT_ID`
         * !Define the project id used to push data to Stackdriver Monitoring
         *
         * !`publish=true`
         * !By default, gathered metrics will be published to Stackdriver when the MeterRegistry is enabled.
         * Use this attribute to selectively disable publication of metrics in some environments.
         *
         * !`publish=true`
         * !By default, gathered metrics will be published to Stackdriver Monitoring when the MeterRegistry is enabled.
         * Use this attribute to selectively disable publication of metrics in some environments.
         *
         * !`step=1m`
         * !The interval at which metrics are sent to Stackdriver Monitoring. The default is 1 minute.
         * !===
         *
         * Other Micrometer configuration attributes can also be specified.
         *
         * @asciidoclet
         */
        // @formatter:on
        @ConfigItem(name = ConfigItem.PARENT)
        Map<String, String> stackdriver;
    }
}
