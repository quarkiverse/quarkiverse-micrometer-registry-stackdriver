package io.quarkiverse.it.micrometer.native_mode;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.search.Search;
import io.smallrye.mutiny.Uni;

@Path("/rest")
public class RestResource {

    private final MeterRegistry registry;

    public RestResource(MeterRegistry registry) {
        this.registry = registry;
    }

    @GET
    @Path("hello")
    @Counted
    @Timed(name = "hello", description = "MP Metrics timer of rest request.")
    public String hello() {
        return "hello";
    }

    @GET
    @Path("goodbye")
    @Counted
    @Timed(name = "goodbye", description = "MP Metrics timer of rest request.")
    public Uni<String> goodbye() {
        return Uni.createFrom().item("goodbye");
    }

    @GET
    @Path("countthem")
    public String countThem() {
        StringBuilder sb = new StringBuilder();
        Collection<Counter> counters = Search.in(registry).counters().stream()
                .filter(x -> x.getId().getName().contains("micrometer.native_mode"))
                .collect(Collectors.toList());
        if (counters.size() > 0) {
            sb.append("Found unexpected counters: \n");
            counters.forEach(x -> sb.append(x.getId().toString()).append("\n"));
        }

        Collection<Timer> timers = Search.in(registry).timers().stream()
                .filter(x -> x.getId().getName().contains("micrometer.native_mode"))
                .collect(Collectors.toList());
        if (timers.size() <= 0) {
            sb.append("Did not find expected timers");
        }

        return sb.length() == 0 ? "ok" : sb.toString();
    }
}
