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

## Environment setup

Server -- remote apache server with mutual auth setup 

Client -- this app

### Server setup

Setup a self signed CA

```openssl genrsa -des3 -out selfsigned-ca.key 2048```

```openssl req -x509 -new -nodes -key selfsigned-ca.key -sha256 -days 1825 -out selfsigned-ca.crt```

#### Certificate - done server side

1) Generate a private key for the client

	```$ openssl genrsa -out selfsigned-cli1.key 2048```
	
	Creates: selfsigned-cli1.key

2) Create a CSR for the client using the previously generated key

	```$ openssl req -new -key selfsigned-cli1.key -out selfsigned-cli1.csr```
	
	Creates: selfsigned-cli1.csr

3) Create the cert

	```$ openssl x509 -req -in selfsigned-cli1.csr -CA selfsigned-ca.crt -CAkey selfsigned-ca.key -set_serial 101 -days 3650 -outform PEM -out selfsigned-cli1.crt```
	
	Creates:  selfsigned-cli1.crt

4) Bundle client certificate and key in to a p12 cert pack

```$ openssl pkcs12 -export -inkey selfsigned-cli1.key -in selfsigned-cli1.crt -out selfsigned-cli1.p12```

#### Install Cert

This is to be done in the keystore that the java app will use

```keytool -v -importkeystore -srckeystore selfsigned-cli.p12 -srcstoretype PKCS12 -destkeystore mutual.jks -deststoretype JKS```

Specify the keystore in the start params :  -Djavax.net.ssl.keyStore=mutual.jks 