package com.searchmetrics.recruitment.exchange

import com.searchmetrics.recruitment.exchange.api.ExchangeRateApiController
import com.searchmetrics.recruitment.exchange.model.ExchangeRate
import com.searchmetrics.recruitment.exchange.query.HistoricalExchangeRateService
import com.searchmetrics.recruitment.exchange.repository.LatestExchangeRateRepository
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
    LatestExchangeRateRepository latestExchangeRateService = Stub()

    @SpringBean
    HistoricalExchangeRateService historicalExchangeRateService = Stub()


    @Autowired
    WebTestClient webClient

    def "should get proper latest rate"(){
        given:
        latestExchangeRateService.getRate() >> Mono.just(testRate)

        expect:
        testRate == webClient.get()
                .uri("/exchangeRate/latest")
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExchangeRate.class)
                .returnResult().getResponseBody()


    }

    def "should get no content"(){
        given:
        latestExchangeRateService.getRate() >> Mono.empty()

        expect:
        webClient.get()
                .uri("/exchangeRate/latest")
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isNoContent()
    }

    def "should get proper history rate"(){
        given:
        historicalExchangeRateService.getHistoricalRates(startDate , endDate) >> Flux.just(testRate)

        expect:
        webClient.get()
                .uri("/exchangeRate?startDate={startDate}&endDate={endDate}",startDate.format(ISO_DATE), endDate.format(ISO_DATE ))
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExchangeRate.class)
                .hasSize(1)
                .contains(testRate)
    }

    def "should return empty list"(){
        given:
        historicalExchangeRateService.getHistoricalRates(startDate , endDate) >> Flux.empty()

        expect:
        webClient.get()
                .uri("/exchangeRate?startDate={startDate}&endDate={endDate}",startDate.format(ISO_DATE), endDate.format(ISO_DATE ))
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExchangeRate.class)
                .hasSize(0)
    }

    def "should return exception on wrong date format"(){
        expect:
        webClient.get()
                .uri("/exchangeRate")
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBodyList(ExchangeRate.class)
                .hasSize(0)
    }
}
