package com.searchmetrics.recruitment.exchange.repository;

import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface ExchangeRateRepository extends R2dbcRepository<ExchangeRate, LocalDateTime> {

        Flux<ExchangeRate> findByDateTimeBetweenOrderByDateTimeAsc(LocalDateTime startDate, LocalDateTime endDate);
}
