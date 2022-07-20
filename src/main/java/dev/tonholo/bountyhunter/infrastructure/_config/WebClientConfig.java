package dev.tonholo.bountyhunter.infrastructure._config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;

@Configuration
@ConditionalOnProperty(name = "webclient.enabled", havingValue = "true")
public class WebClientConfig {

    @Getter
    private final Integer connectionTimeoutMillis;

    public WebClientConfig(
            @Value("${webclient.timeoutSeconds}") final Integer connectionTimeoutSeconds) {
        this.connectionTimeoutMillis = connectionTimeoutSeconds * 1000;
    }

    @Bean
    @Primary
    public WebClient webClient() {
        final var httpClient
                = HttpClient
                .create()
                .option(CONNECT_TIMEOUT_MILLIS, connectionTimeoutMillis)
                .responseTimeout(Duration.ofMillis(connectionTimeoutMillis))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(connectionTimeoutMillis, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(connectionTimeoutMillis, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}