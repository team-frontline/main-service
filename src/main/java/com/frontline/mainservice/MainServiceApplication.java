package com.frontline.mainservice;

import com.frontline.mainservice.config.Config;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.frontline.certInstaller.certOperation.KeyStoreManager;
import org.frontline.certInstaller.mschainOperation.CertManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MainServiceApplication {

    public static void main(String[] args) {/* String commonName, String organizationUnit, String organizationName,
         String localityName, String stateName, String country */
        Map<String, String> csrParamMap = new HashMap<String, String>();
        csrParamMap.put("commonName", "ms-service-service1");
        csrParamMap.put("organizationUnit", "MSChain");
        csrParamMap.put("organizationName", "Frontline.org");
        csrParamMap.put("localityName", "Colombo");
        csrParamMap.put("stateName", "West");
        csrParamMap.put("country", "SL");
        Config.setKeyStoreName("ms-service-store1.jks");
        Config.setKeyStorePassword("pass123");


        try {
            File ks = new File(Config.SOURCE_PATH + Config.getKeyStoreName());
            if (!ks.exists()) {
                CertManager certManager = new CertManager();
                certManager.initiateCertificate(Config.SOURCE_PATH, "ms-service-service1", csrParamMap);
                KeyStoreManager.installCert(Config.SOURCE_PATH, certManager.getKeyPair()
                        , "ms-service-service1.org", Config.getKeyStorePassword(), certManager.getCertificate(), Config.getKeyStoreName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SpringApplication.run(MainServiceApplication.class, args);
        }

    }

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        KeyStore keyStore;
        HttpComponentsClientHttpRequestFactory requestFactory = null;

        try {
            keyStore = KeyStore.getInstance("JKS");
//            ClassPathResource classPathResource = new ClassPathResource("/home/supimi/poc/microservices-application/main-service/src/main/resources/ms-service-store1.jks");
//            InputStream inputStream = classPathResource.getInputStream();
            InputStream is = new FileInputStream(new File(Config.SOURCE_PATH + Config.getKeyStoreName()));
            keyStore.load(is, Config.getKeyStorePassword().toCharArray());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
                    .loadTrustMaterial(new File(Config.SOURCE_PATH + Config.getKeyStoreName()))
                    .loadKeyMaterial(keyStore, Config.getKeyStorePassword().toCharArray()).build(),
                    NoopHostnameVerifier.INSTANCE);

            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                    .setMaxConnTotal(5)
                    .setMaxConnPerRoute(5)
                    .build();

            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setReadTimeout(30000);
            requestFactory.setConnectTimeout(30000);

            restTemplate.setRequestFactory(requestFactory);
        } catch (Exception exception) {
            System.out.println("Exception Occur while creating restTemplate " + exception);
            exception.printStackTrace();
        }
        return restTemplate;
    }
}
