#!/bin/bash
keytool -import -trustcacerts -alias ca1.com -file certCA1.pem -keystore ms-service-store1.jks

keytool -import -trustcacerts -alias ms1.org  -file ms1_cert.pem -keystore ms-service-store1.jks

keytool -list -v -keystore ms-service-store1.jks

keytool -import -trustcacerts -alias postman-test.org -file postman-test.pem -keystore ms-service-store1.jks

