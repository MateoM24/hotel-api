# Hotel API
Project is written with Java 16.

I was inspired by hexagonal architecture. That's why Domain module is the main part of the application and there is supplementary module Infrastructure that provides implementation of database, REST endpoint and application entry point. It could be split into 3 separate module but for such small application it's overkill.

### How to run it?
Being in folder 'infrastructure' run command:
`mvn spring-boot:run`

### Tests
There are test only in Domain module

### Swagger-UI
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/hotel-booking-controller/calculateHotelAccommodation

### Requisites to compile:
JDK 16
