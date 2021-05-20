package com.searchmetrics.recruitment.exchange.repository;

import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class LatestExchangeRateRepository {

    private static final Logger logger = LoggerFactory.getLogger(LatestExchangeRateRepository.class);

    private AtomicReference<ExchangeRate> latestRate = new AtomicReference<>();

    public Mono<ExchangeRate> getRate() {
        return Mono.justOrEmpty(latestRate.get());
    }

    public void setLatestRate(ExchangeRate exchangeRate) {
        logger.debug("Updating latestRate with : " + exchangeRate);
        latestRate.set(exchangeRate);
    }
}
