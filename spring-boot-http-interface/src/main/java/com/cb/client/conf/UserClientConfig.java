package com.cb.client.conf;

import com.cb.client.UserClient;
import com.cb.exception.ExternalCommunicationException;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "user-client")
@Setter
public class UserClientConfig {

    private String host;
    private Integer maxConnections;
    private Integer maxIdleTimeSeconds;
    private Integer maxLifeTimeSeconds;
    private Long responseTimeoutMilis;
    private Integer retries;
    private Integer retryDelayMilis;

    private HttpClient httpClient() {
        ConnectionProvider connectionProvider = ConnectionProvider.builder("client").maxConnections(
                maxConnections).maxIdleTime(Duration.ofSeconds(maxIdleTimeSeconds)).maxLifeTime(
                Duration.ofSeconds(maxLifeTimeSeconds)).build();
        return HttpClient.create(connectionProvider).protocol(HttpProtocol.HTTP11);
    }

    private WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .filter(responseTimeout(responseTimeoutMilis))
                .filter(retry(retries, retryDelayMilis))
                .build();
    }

    @Bean
    public UserClient userClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(host)
                .defaultStatusHandler(
                        httpStatusCode -> HttpStatus.NOT_FOUND == httpStatusCode,
                        response -> Mono.empty())
                .defaultStatusHandler(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new ExternalCommunicationException(response.statusCode().value())))
                .build();

        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build()
                .createClient(UserClient.class);
    }

    // Timeout
    private static ExchangeFilterFunction responseTimeout(Long responseTimeout) {
        return (request, next) -> next.exchange(request).timeout(Duration.ofMillis(responseTimeout));
    }

    // Retry
    private static ExchangeFilterFunction retry(Integer retries, Integer retryDelayMilis) {
        return (request, next) -> next.exchange(request).retryWhen(
                Retry.backoff(retries, Duration.ofMillis(retryDelayMilis)));
    }
}
