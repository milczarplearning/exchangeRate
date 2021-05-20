package com.searchmetrics.recruitment.exchange.model;

import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


public class ExchangeRate {

    @Column("date_time")
    private LocalDateTime dateTime;

    @Column("rate")
    private BigDecimal rate;


    public ExchangeRate(BigDecimal rate, LocalDateTime dateTime) {
        this.rate = rate;
        this.dateTime = dateTime;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return dateTime.equals(that.dateTime) && rate.equals(that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, rate);
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "dateTime=" + dateTime +
                ", rate=" + rate +
                '}';
    }
}
