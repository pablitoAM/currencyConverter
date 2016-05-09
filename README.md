# Currency Converter

A currency converter converter application as a software test.

![Travis](https://travis-ci.org/pablitoAM/currencyConverter.svg?branch=master)

The application has been implemented with microservices arquitecture patterns. It has a Discovery Service, an API Gateway and an OAuth2 login system wit Jwt tokens. All the communications between de services have been implemented via REST apis and the front-end has been implemented with Spring MVC as it was a requirement.

Here is the architectural design.

![Architectural Design](https://github.com/pablitoAM/currencyConverter/blob/master/zoo.png)


In order to test it locally, checkout the repository and run:
```
$ mvn clean install
```
After that copy the jar files with no extension such as "front-web.jar", which can be found in each target folder, into a common folder. Also copy the file startup.sh there.

Finally run:
```
$ sh startup.sh
```
To test the application open the explorer with the url http://localhost:8080 for local instances.

> This project is meant for educational purposes and can be examined at: http://zoo.pabloam.com
