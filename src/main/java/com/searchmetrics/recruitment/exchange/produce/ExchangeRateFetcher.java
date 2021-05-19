package com.searchmetrics.recruitment.exchange.produce;

import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import reactor.core.publisher.Mono;

public interface ExchangeRateFetcher {
    public Mono<ExchangeRate> fetch(String base, String currency);
}
