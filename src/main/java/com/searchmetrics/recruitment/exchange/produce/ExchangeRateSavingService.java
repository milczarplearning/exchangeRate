package com.searchmetrics.recruitment.exchange.produce;

import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import com.searchmetrics.recruitment.exchange.repository.ExchangeRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class ExchangeRateSavingService {

    private ExchangeRateRepository exchangeRateRepository;

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateProducer.class);

    @Autowired
    public ExchangeRateSavingService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }


    @Transactional
    public Flux<ExchangeRate> saveAll(List<ExchangeRate> exchangeRate){
        return exchangeRateRepository.saveAll(exchangeRate)
                .doOnError(ex -> logger.error("Exception on saving Bitcoin rates.\n"+ ex.getMessage()))
                .onErrorResume(e -> Flux.empty());
    }
}
