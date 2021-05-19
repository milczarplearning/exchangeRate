package com.searchmetrics.recruitment.exchange.produce;

import com.searchmetrics.recruitment.exchange.repository.LatestExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.Duration;

import static com.searchmetrics.recruitment.exchange.model.SupportedCurrency.BTC;
import static com.searchmetrics.recruitment.exchange.model.SupportedCurrency.USD;

@Component
public class ExchangeRateProducer {

    private ExchangeRateSavingService exchangeRateService;
    private ExchangeRateFetcher exchangeRateFetcher;
    private LatestExchangeRateRepository latestExchangeRateRepository;
    private Long fetchRateInMiliseconds;


    @Autowired
    public ExchangeRateProducer(ExchangeRateSavingService exchangeRateService, ExchangeRateFetcher exchangeRateFetcher, LatestExchangeRateRepository latestExchangeRateRepository, @Value("${fetchRate.in.milliseconds}") Long fetchRateInMiliseconds) {
        this.exchangeRateService = exchangeRateService;
        this.exchangeRateFetcher = exchangeRateFetcher;
        this.latestExchangeRateRepository = latestExchangeRateRepository;
        this.fetchRateInMiliseconds = fetchRateInMiliseconds;
    }


    @PostConstruct
    public void processExchangeRates() {
        Flux<Long> flux = Flux.interval(Duration.ofMillis(fetchRateInMiliseconds), Schedulers.boundedElastic());
        int bufferSize = (int) Math.min(20, 5000 / fetchRateInMiliseconds);
        flux.flatMap(input -> exchangeRateFetcher.fetch(BTC.name(), USD.name()))
                .doOnNext(value -> latestExchangeRateRepository.setLatestRate(value))
                .buffer(bufferSize)
                .flatMap(value -> exchangeRateService.saveAll(value))
                .subscribe();
    }
}
