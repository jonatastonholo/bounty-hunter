package dev.tonholo.bountyhunter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
public class BountyHunterApplication {

    public static void main(String[] args) throws InterruptedException {
        try (var context = SpringApplication.run(BountyHunterApplication.class, args)) {
            log.info("Bounty Hunter initialized -> PID: {}", ProcessHandle.current().pid());
            final CountDownLatch closeLatch = context.getBean(CountDownLatch.class);
            Runtime.getRuntime().addShutdownHook(new Thread(closeLatch::countDown));
            closeLatch.await();
        }
    }

    @Bean
    public CountDownLatch closeLatch() {
        return new CountDownLatch(1);
    }
}
