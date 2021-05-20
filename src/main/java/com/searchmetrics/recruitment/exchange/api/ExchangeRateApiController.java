package com.searchmetrics.recruitment.exchange.api;

import com.searchmetrics.recruitment.exchange.model.ExchangeRate;
import com.searchmetrics.recruitment.exchange.query.QueryExchangeRateService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@RestController
@RequestMapping("/exchangeRate/BTC-USD")
@Validated
public class ExchangeRateApiController {

    private QueryExchangeRateService queryExchangeRateService;

    @Autowired
    public ExchangeRateApiController(QueryExchangeRateService queryExchangeRateService) {
        this.queryExchangeRateService = queryExchangeRateService;
    }

    @GetMapping(path = "/latest")
    public Mono<ResponseEntity<ExchangeRate>> latest() {
        return queryExchangeRateService.getRate()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping()
    public Flux<ExchangeRate> historical(
            @ApiParam(
                    name = "startDate",
                    format = "YYYY-MM-dd",
                    example = "2021-05-01",
                    required = true) @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @ApiParam(
                    name = "endDate",
                    format = "YYYY-MM-dd",
                    example = "2021-05-10",
                    required = true)
            @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return queryExchangeRateService.getHistoricalRates(startDate, endDate);
    }
}
