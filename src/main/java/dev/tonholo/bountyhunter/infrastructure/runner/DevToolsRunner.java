package dev.tonholo.bountyhunter.infrastructure.runner;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.blockhound.BlockHound;
import reactor.tools.agent.ReactorDebugAgent;

import java.util.Objects;

@Component
@Slf4j
public class DevToolsRunner implements ApplicationRunner {
    private final boolean enableBlockHound;
    private final boolean justPrintStackTrace;

    @Autowired
    public DevToolsRunner(
            @Value("${blockHound.enabled}") final Boolean enableBlockHound,
            @Value("${blockHound.justPrintStackTrace}") final Boolean justPrintStackTrace) {
        Objects.requireNonNull(enableBlockHound, "Failed to load env var BLOCK_HOUND_ENABLED");
        Objects.requireNonNull(justPrintStackTrace, "Failed to load env var BLOCK_HOUND_JUST_PRINT_STACKTRACE");

        this.enableBlockHound = enableBlockHound;
        this.justPrintStackTrace = justPrintStackTrace;
    }

    @Override
    public void run(final ApplicationArguments args) {
        log.info("Loading debug tools");

        // It's cool to use in production
        ReactorDebugAgent.init();

        // Not good to run in production!!
        if (enableBlockHound) {
            log.info("Loading exclusive develop profile debug tools");
            BlockHound.install(
                    builder -> {
                        if (justPrintStackTrace) {
                            builder.blockingMethodCallback(it
                                    -> new Exception(it.toString()).printStackTrace());
                        }
                        builder.allowBlockingCallsInside("reactor.core.scheduler.BoundedElasticScheduler", "dispose");
                    }
            );
        }
    }
}
