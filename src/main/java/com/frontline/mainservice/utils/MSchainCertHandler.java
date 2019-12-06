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
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

public class MSchainCertHandler {
    final static Logger logger = Logger.getLogger(MSchainCertHandler.class.toString());

    public  static boolean validateCertificate(String cn, X509Certificate x509Certificate) throws IOException, CertificateEncodingException {
        RestTemplate restTemplate = new RestTemplate();

        String certString = CertX509Handler.stringFromCert(x509Certificate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("subjectName", cn);
        map.add("cert",certString);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        logger.info("Validating certificate through MSChain...");
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(Config.LUTHER_SERVICE_URL, request , JsonNode.class );
        System.out.println(response);
        boolean validity = response.getBody().get("data").get("validity").asBoolean();
        logger.info("from mschain validity checker");
        logger.info(response.getBody().get("data").get("validity").toString());
        System.out.println("from mschain validity checker");
        System.out.println(response.getBody().get("data").get("validity"));
        System.out.println(response.getBody().get("sentCert"));
        System.out.println(response.getBody().get("data").get("certString"));
        System.out.println(certString.equals(response.getBody().get("sentCert")));
        if(certString.equals(response.getBody().get("sentCert"))){
            System.out.println("certs are equal");
        }else{
            System.out.println("certs are not equal");
        }

        if(validity){
            return true;
        }else{
            return false;
        }
    }
}
