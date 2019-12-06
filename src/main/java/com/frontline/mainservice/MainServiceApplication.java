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
import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MainServiceApplication {

    public static void main(String[] args) {/* String commonName, String organizationUnit, String organizationName,
         String localityName, String stateName, String country */
        Map<String, String> csrParamMap = new HashMap<String, String>();
        csrParamMap.put("commonName", "msg1-service2.org");
        csrParamMap.put("organizationUnit", "MSChain");
        csrParamMap.put("organizationName", "Frontline.org");
        csrParamMap.put("localityName", "Colombo");
        csrParamMap.put("stateName", "West");
        csrParamMap.put("country", "SL");
        Config.setKeyStoreName("msg-store2.jks");
        Config.setKeyStorePassword("pass123");


        try {
            File ks = new File(Config.SOURCE_PATH + Config.getKeyStoreName());
            if (!ks.exists()) {
                CertManager certManager = new CertManager();
                certManager.initiateCertificate(Config.SOURCE_PATH, "msg-service2", csrParamMap);
                KeyStoreManager.installCert(Config.SOURCE_PATH, certManager.getKeyPair()
                        , "msg1-service2.org", Config.getKeyStorePassword(), certManager.getCertificate(), Config.getKeyStoreName());
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
            keyStore = KeyStore.getInstance("jks");
            ClassPathResource classPathResource = new ClassPathResource(Config.getKeyStoreName());
            InputStream inputStream = classPathResource.getInputStream();
            keyStore.load(inputStream, Config.getKeyStorePassword().toCharArray());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .loadKeyMaterial(keyStore, Config.getKeyStorePassword().toCharArray()).build(),
                    NoopHostnameVerifier.INSTANCE);

            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                    .setMaxConnTotal(Integer.valueOf(5))
                    .setMaxConnPerRoute(Integer.valueOf(5))
                    .build();

            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setReadTimeout(Integer.valueOf(10000));
            requestFactory.setConnectTimeout(Integer.valueOf(10000));

            restTemplate.setRequestFactory(requestFactory);
        } catch (Exception exception) {
            System.out.println("Exception Occur while creating restTemplate " + exception);
            exception.printStackTrace();
        }
        return restTemplate;
    }
}
