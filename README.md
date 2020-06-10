## What? 

Just a dumb little app that can be used to test mutual authentication

It has 2 endpoints:

* /hostname - returns the hostname of the local machine
* /machineRequest - querable endpoint to return the host of another machine running this same application

Example: https://localhost/machineRequest?hostname.of.other.server

Would return the hostname of the other server.

## How

``mvn clean install
java -jar target/demo-0.0.1-SNAPSHOT.jar``

