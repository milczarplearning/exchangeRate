package com.searchmetrics.recruitment.exchange.produce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
public class IntervalExchangeRateProducer {

    private ExchangeRateProducer exchangeRateProducer;
    private Long fetchRateInMiliseconds;

    public IntervalExchangeRateProducer(ExchangeRateProducer exchangeRateProducer, @Value("${fetchRate.in.milliseconds}") Long fetchRateInMiliseconds) {
        this.exchangeRateProducer = exchangeRateProducer;
        this.fetchRateInMiliseconds = fetchRateInMiliseconds;
    }

    @PostConstruct
    public void processExchangeRates() {
        Flux<Long> flux = Flux.interval(Duration.ofMillis(fetchRateInMiliseconds), Schedulers.boundedElastic());
        int bufferSize = (int) Math.min(20, 5000 / fetchRateInMiliseconds);
        exchangeRateProducer.setUpProduction(flux, bufferSize)
                .subscribe();
    }
}
