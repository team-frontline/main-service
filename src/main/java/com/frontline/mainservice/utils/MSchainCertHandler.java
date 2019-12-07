package com.frontline.mainservice.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.frontline.mainservice.config.Config;
import org.frontline.certInstaller.utils.KeyOperator;
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
import java.util.List;
import java.util.logging.Logger;

public class MSchainCertHandler {
    final static Logger logger = Logger.getLogger(MSchainCertHandler.class.toString());

    public static boolean validateCertificate(String cn, X509Certificate x509Certificate) throws IOException, CertificateEncodingException {
        RestTemplate restTemplate = new RestTemplate();

        String certString = CertX509Handler.stringFromCert(x509Certificate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("subjectName", cn);
        map.add("cert", certString);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        logger.info("Validating certificate through MSChain...");
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(Config.LUTHER_SERVICE_URL, request, JsonNode.class);
        boolean validity = response.getBody().get("data").get("validity").asBoolean();
        System.out.println(response);
        logger.info("from mschain validity checker");

        if (validity) {
            logger.info("Cert is validated as OK");
            return true;
        } else {
            logger.info("Cert is validated as Invalid");
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
}
