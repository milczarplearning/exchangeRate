package com.searchmetrics.recruitment.exchange.repository

import com.searchmetrics.recruitment.exchange.model.ExchangeRate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDateTime

@SpringBootTest
class ExchangeRateRepositoryIntegrationTest extends Specification {

    @Autowired
    ExchangeRateRepository exchangeRateRepository

    def "should save and retrieve data"() {
        given:
        def exchangeRate = new ExchangeRate(new BigDecimal(22), LocalDateTime.now())

        expect:
        exchangeRateRepository.save(exchangeRate).block() == exchangeRate
        exchangeRateRepository.findAll().collectList().block().size() == 1
    }
}
