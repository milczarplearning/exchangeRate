package com.searchmetrics.recruitment.exchange.produce;

import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import com.searchmetrics.recruitment.exchange.produce.fetch.ExchangeRateFetcher;
import com.searchmetrics.recruitment.exchange.repository.LatestExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static com.searchmetrics.recruitment.exchange.model.SupportedCurrency.BTC;
import static com.searchmetrics.recruitment.exchange.model.SupportedCurrency.USD;

@Component
public class ExchangeRateProducer {

    private ExchangeRateSavingService exchangeRateService;
    private ExchangeRateFetcher exchangeRateFetcher;
    private LatestExchangeRateRepository latestExchangeRateRepository;


    @Autowired
    public ExchangeRateProducer(ExchangeRateSavingService exchangeRateService, ExchangeRateFetcher exchangeRateFetcher, LatestExchangeRateRepository latestExchangeRateRepository) {
        this.exchangeRateService = exchangeRateService;
        this.exchangeRateFetcher = exchangeRateFetcher;
        this.latestExchangeRateRepository = latestExchangeRateRepository;
    }


    Flux<ExchangeRate> setUpProduction(Flux<Long> flux, int bufferSize) {
        return flux.flatMap(input -> exchangeRateFetcher.fetch(BTC.name(), USD.name()))
                .doOnNext(value -> latestExchangeRateRepository.setLatestRate(value))
                .buffer(bufferSize)
                .flatMap(value -> exchangeRateService.saveAll(value));
    }
}
