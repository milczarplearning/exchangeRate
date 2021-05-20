package com.searchmetrics.recruitment.exchange.repository;

import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class LatestExchangeRateRepository {

    private AtomicReference<ExchangeRate> latestRate = new AtomicReference<>();
    public Mono<ExchangeRate> getRate() {
        return  Mono.justOrEmpty(latestRate.get());
    }
    public void setLatestRate(ExchangeRate exchangeRate){
        latestRate.set(exchangeRate);
    }
}
