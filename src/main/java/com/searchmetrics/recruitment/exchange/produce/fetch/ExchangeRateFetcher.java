package com.searchmetrics.recruitment.exchange.produce.fetch;

import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import reactor.core.publisher.Mono;

public interface ExchangeRateFetcher {
    Mono<ExchangeRate> fetch(String base, String currency);
}
