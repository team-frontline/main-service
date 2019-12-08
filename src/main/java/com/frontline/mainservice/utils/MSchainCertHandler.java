package com.frontline.mainservice.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.frontline.mainservice.config.Config;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MSchainCertHandler {
    final static Logger logger = Logger.getLogger(MSchainCertHandler.class.toString());

    public static boolean validateCertificate(java.lang.String cn, X509Certificate x509Certificate) throws IOException, CertificateEncodingException {
        RestTemplate restTemplate = new RestTemplate();

        java.lang.String certString = CertX509Handler.stringFromCert(x509Certificate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<java.lang.String, java.lang.String> map = new LinkedMultiValueMap<java.lang.String, java.lang.String>();
        map.add("subjectName", cn);
        map.add("cert", certString);

        HttpEntity<MultiValueMap<java.lang.String, java.lang.String>> request = new HttpEntity<MultiValueMap<java.lang.String, java.lang.String>>(map, headers);

        logger.info("Validating certificate through MSChain...");
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(Config.LUTHER_SERVICE_URL, request, JsonNode.class);
        boolean validity = response.getBody().get("data").get("validity").asBoolean();
                logger.info("from mschain validity checker");

        if (validity) {
            logger.info("Cert is validated as OK");
            return true;
        } else {
            logger.info("Cert is validated as Invalid");
            return false;
        }
    }

    public static boolean validateCertificate_demo(java.lang.String cn, X509Certificate x509Certificate, StateArray stateArray) throws IOException, CertificateEncodingException {
        RestTemplate restTemplate = new RestTemplate();

        java.lang.String certString = CertX509Handler.stringFromCert(x509Certificate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<java.lang.String, java.lang.String> map = new LinkedMultiValueMap<java.lang.String, java.lang.String>();
        map.add("subjectName", cn);
        map.add("cert", certString);

        HttpEntity<MultiValueMap<java.lang.String, java.lang.String>> request = new HttpEntity<MultiValueMap<java.lang.String, java.lang.String>>(map, headers);

        logger.info("Validating certificate through MSChain...");
        boolean validity = false;
        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(Config.LUTHER_SERVICE_URL, request, JsonNode.class);
            stateArray.sendCertToMSchain.setStateMessage(StateMessage.SUCCESS);
            validity = response.getBody().get("data").get("validity").asBoolean();
            logger.info("from mschain validity checker");
            stateArray.receivedMSChainResponse.setStateMessage(StateMessage.SUCCESS);
        } catch (Exception e) {
            stateArray.sendCertToMSchain.setStateMessage(StateMessage.FAILED);
            stateArray.receivedMSChainResponse.setStateMessage(StateMessage.FAILED);
        }

        if (validity) {
            logger.info("Cert is validated as OK");
            stateArray.validateServerCertificate.setStateMessage(StateMessage.SUCCESS);
            return true;
        } else {
            logger.info("Cert is validated as Invalid");
            stateArray.validateServerCertificate.setStateMessage(StateMessage.FAILED);
            return false;
        }
    }

    public static boolean validateServerCert(HttpHeaders httpHeaders) throws IOException, CertificateException {
        logger.info("Validating sever certificate");
        X509Certificate serverCert = CertX509Handler.getHeaderCert(httpHeaders);
        boolean serverCertValid = MSchainCertHandler.
                validateCertificate(CertX509Handler.getDomainName(serverCert), serverCert);
        logger.info("Server certificate validated as " + serverCertValid);
        return serverCertValid;
    }

    public static boolean validateServerCert_demo(HttpHeaders httpHeaders, StateArray stateArray) {
        logger.info("Validating sever certificate");
        boolean serverCertValid = false;
        try {
            X509Certificate serverCert = CertX509Handler.getHeaderCert(httpHeaders);
            serverCertValid = MSchainCertHandler.
                    validateCertificate_demo(CertX509Handler.getDomainName(serverCert), serverCert, stateArray);
            logger.info("Server certificate validated as " + serverCertValid);
        } catch (Exception e) {
            stateArray.sendCertToMSchain.setStateMessage(StateMessage.FAILED);
            stateArray.receivedMSChainResponse.setStateMessage(StateMessage.FAILED);
            stateArray.validateServerCertificate.setStateMessage(StateMessage.FAILED);
        }
        return serverCertValid;
    }
}
