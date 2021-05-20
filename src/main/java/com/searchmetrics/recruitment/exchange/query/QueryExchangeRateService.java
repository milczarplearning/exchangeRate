package com.searchmetrics.recruitment.exchange.query;

import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import com.searchmetrics.recruitment.exchange.repository.ExchangeRateRepository;
import com.searchmetrics.recruitment.exchange.repository.LatestExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class QueryExchangeRateService {

    private ExchangeRateRepository exchangeRateRepository;
    private LatestExchangeRateRepository latestExchangeRateRepository;

    @Autowired
    public QueryExchangeRateService(ExchangeRateRepository exchangeRateRepository, LatestExchangeRateRepository latestExchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.latestExchangeRateRepository = latestExchangeRateRepository;
    }

    @Transactional(readOnly = true)
    public Flux<ExchangeRate> getHistoricalRates(LocalDate startDate, LocalDate endDate) {
        return exchangeRateRepository.findByDateTimeBetweenOrderByDateTimeAsc(startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1));
    }

    public Mono<ExchangeRate> getRate() {
        return latestExchangeRateRepository.getRate();
    }
}