# Searchemtrics coding task

Please develop a service, that constantly checks the currency exchange rate from Bitcoin to US-Dollar (1 Bitcoin = x
USD). Requirements:

- The check period has to be configurable
- The service must have a REST API with the following endpoints:
- Get latest rate
- Get historical rates from startDate to endDate
- Please describe in a few sentences and/or with a diagram how you planned the project and architecture.
- should be coded in Java

### Initial Plan

#### Tech Stack:

- Java 11
- Source of data  (coinbase API - well designed, well documented, stable)
- Spring boot (webflux)
- Some "Time series" db or Cassandra (as persistance for historical data)
- Kafka:
  - Producer responsible for fetching the data from coinbase and pushing to the topic
  - Consumers responsible for fetching the data from the topic and saving to db and to latest exchangeRate

That in my opinion should be the final solution. Ready for production.

However I won't be able to do it in 8-12 hours I that was the plan. I wanted also solution that is easy runnable (no
docker involved)

#### Reality

The solution is not ready for production. It is easy runnable and it "resembles"  pattern of desired solution.

#### Tech Stack:

- Java 11
- Source of data  (coinbase API - well designed, well documented, stable)
- Spring boot (webflux)
- H2 database (storing historical data)
- Reactive Streams using Project Reactor for feeding the db
- Swagger for API description

##### Workplan

1. Implement Web API and setup testing framework
2. Implement persistance layer db and latest Exchange Rate value
3. Add Swagger
4. Implement fetching and saving the exchangeRate data.

### Run it

Requirements:

- OpenJDK 11

To build the application run:

`./mvnw clean package`

Then run it:

`java -jar target/exchangeRate-0.0.1-SNAPSHOT.jar`

Access via swagger ui:

`http://localhost:8080/swagger-ui/`

##### What needs to be changed to be production ready

1. Create API model fo ExchangeRate so that is decoopled from Exchange Rate and so that it has fields "base" ("BTC")
   and "currency" ("USD")
2. Improve API error handling and improve Swagger Endpoints description
3. Add logback configuration for dev and production environment with saving logs to files with proper time and size
   based policy
4. Change DB to timescaleDB or QuantDB (they are SQL compliant)
5. Add monitoring
6. Reimplements saving exchangeRates so that it uses kafka
7. Prepare integration test using testcontainers
8. Prepare docker, docker compose fields for setting up the environment 


