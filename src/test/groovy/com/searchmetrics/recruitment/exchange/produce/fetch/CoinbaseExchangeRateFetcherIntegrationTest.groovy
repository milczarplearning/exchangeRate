package com.searchmetrics.recruitment.exchange.produce.fetch


import com.searchmetrics.recruitment.exchange.model.ExchangeRate
import com.searchmetrics.recruitment.exchange.produce.fetch.CoinbaseExchangeRateFetcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Ignore
import spock.lang.Specification


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoinbaseExchangeRateFetcherIntegrationTest extends Specification {

    @Autowired
    CoinbaseExchangeRateFetcher coinbaseExchangeRateFetcher

    @Ignore
    def "should properly get exchange rate from coinbase"(){
        when:
        ExchangeRate exchangeRate = coinbaseExchangeRateFetcher.fetch("BTC", "USD").block()

        then:
        exchangeRate.getRate() != null
        exchangeRate.getDateTime() != null
    }
}
