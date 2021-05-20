package com.searchmetrics.recruitment.exchange.produce

import com.searchmetrics.recruitment.exchange.model.ExchangeRate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import spock.lang.Specification

import java.time.LocalDate

import static java.time.format.DateTimeFormatter.ISO_DATE
import static org.springframework.http.HttpHeaders.ACCEPT

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExchangeRateProducerIntegrationTest extends Specification {

    @Autowired
    private ExchangeRateProducer exchangeRateProducer

    @Autowired
    WebTestClient webClient

    def "should produce Exchange Rates and save them to db"() {
        given:
        LocalDate today = LocalDate.now()
        Flux<Long> flux = Flux.just(1L, 2L, 3L)
        int bufferSize = 2

        when:
        exchangeRateProducer.setUpProduction(flux, bufferSize).blockLast()

        then:

        webClient.get()
                .uri("/exchangeRate/BTC-USD/?startDate={startDate}&endDate={endDate}", today.format(ISO_DATE), today.format(ISO_DATE))
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExchangeRate.class)
                .hasSize(3)

        webClient.get()
                .uri("/exchangeRate/BTC-USD/latest")
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExchangeRate.class)

    }
}
