DROP TABLE IF EXISTS exchange_rate;
CREATE TABLE exchange_rate
(
    date_time timestamp NOT NULL,
    rate      decimal   NOT NULL
);
CREATE INDEX date_time_index ON exchange_rate(date_time);