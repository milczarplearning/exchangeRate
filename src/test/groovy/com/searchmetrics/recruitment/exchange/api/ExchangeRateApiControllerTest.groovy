package com.searchmetrics.recruitment.exchange.api

import com.searchmetrics.recruitment.exchange.model.ExchangeRate
import com.searchmetrics.recruitment.exchange.query.QueryExchangeRateService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

import static java.time.format.DateTimeFormatter.ISO_DATE
import static org.springframework.http.HttpHeaders.ACCEPT

@AutoConfigureWebTestClient(timeout = "3600")
@WebFluxTest(controllers = [ExchangeRateApiController.class])
class ExchangeRateApiControllerTest extends Specification {

    ExchangeRate testRate = new ExchangeRate(new BigDecimal(1), LocalDateTime.now())
    LocalDate startDate = LocalDate.now().minusDays(10)
    LocalDate endDate = LocalDate.now().minusDays(5)

    @SpringBean
    QueryExchangeRateService queryExchangeRateService = Stub()


    @Autowired
    WebTestClient webClient

    def "should get proper latest rate"(){
        given:
        queryExchangeRateService.getRate() >> Mono.just(testRate)

        expect:
        testRate == webClient.get()
                .uri("/exchangeRate/BTC-USD/latest")
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExchangeRate.class)
                .returnResult().getResponseBody()


    }

    def "should get no content"(){
        given:
        queryExchangeRateService.getRate() >> Mono.empty()

        expect:
        webClient.get()
                .uri("/exchangeRate/BTC-USD/latest")
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isNoContent()
    }

    def "should get proper history rate"(){
        given:
        queryExchangeRateService.getHistoricalRates(startDate, endDate) >> Flux.just(testRate)

        expect:
        webClient.get()
                .uri("/exchangeRate/BTC-USD/?startDate={startDate}&endDate={endDate}", startDate.format(ISO_DATE), endDate.format(ISO_DATE))
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExchangeRate.class)
                .hasSize(1)
                .contains(testRate)
    }

    def "should return empty list"(){
        given:
        queryExchangeRateService.getHistoricalRates(startDate, endDate) >> Flux.empty()

        expect:
        webClient.get()
                .uri("/exchangeRate/BTC-USD/?startDate={startDate}&endDate={endDate}", startDate.format(ISO_DATE), endDate.format(ISO_DATE))
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExchangeRate.class)
                .hasSize(0)
    }

}
