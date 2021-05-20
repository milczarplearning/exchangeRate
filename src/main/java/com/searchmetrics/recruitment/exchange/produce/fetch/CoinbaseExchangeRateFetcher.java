package com.searchmetrics.recruitment.exchange.produce.fetch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Component
public class CoinbaseExchangeRateFetcher implements ExchangeRateFetcher {

    private static final String URL = "https://api.coinbase.com/v2/prices/{base}-{currency}/spot";

    private static final Logger logger = LoggerFactory.getLogger(CoinbaseExchangeRateFetcher.class);

    @Override
    public Mono<ExchangeRate> fetch(String base, String currency) {
        return WebClient.create()
                .get()
                .uri(URL, base, currency)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchangeToMono(
                    response -> {
                        if (response.statusCode()
                                .equals(HttpStatus.OK)) {
                            return response.bodyToMono(CoinbaseResponse.class)
                                    .map(coinbaseResponse -> new ExchangeRate(coinbaseResponse.data.amount, LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));
                        } else {
                            return response.createException()
                                    .flatMap(Mono::error);
                        }
                    }
                )
                .timeout(Duration.ofSeconds(5))
                .doOnError(Exception.class, ex -> logger.error("Error on fetching exchangeRate fron Coinbase.\n"+ ex.getMessage()));
    }

    static class CoinbaseResponse {
        CoinbaseExchangeRate data;

        @JsonCreator
        CoinbaseResponse(@JsonProperty("data") CoinbaseExchangeRate data) {
            this.data = data;
        }
    }


    static class CoinbaseExchangeRate {
        String base;
        String currency;
        BigDecimal amount;

        @JsonCreator
        CoinbaseExchangeRate(@JsonProperty("base") String base, @JsonProperty("currency") String currency, @JsonProperty("amount") BigDecimal amount) {
            this.base = base;
            this.currency = currency;
            this.amount = amount;
        }
    }
}
