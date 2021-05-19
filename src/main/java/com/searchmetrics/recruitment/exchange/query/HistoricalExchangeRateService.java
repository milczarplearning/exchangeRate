package com.searchmetrics.recruitment.exchange.query;

import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import com.searchmetrics.recruitment.exchange.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
public class HistoricalExchangeRateService {

    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public HistoricalExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Transactional(readOnly = true)
    public Flux<ExchangeRate> getHistoricalRates(LocalDate startDate, LocalDate endDate) {
        return exchangeRateRepository.findByDateTimeBetweenOrderByDateTimeAsc(startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1));
    }
}