package com.frontline.mainservice.service;

import com.frontline.mainservice.config.Config;
import com.frontline.mainservice.model.Item;
import com.frontline.mainservice.utils.MSchainCertHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.security.KeyStore;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    RestTemplate restTemplate;

    public List<Item> getItems() {
        List items;
        try {
            String keyStorePassword = Config.getKeyStorePassword();

            boolean willProceed = true;
            if (willProceed) {
//                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                keyStore.load(new FileInputStream(new File(Config.SOURCE_PATH + Config.getKeyStoreName())),
//                        keyStorePassword.toCharArray());
//
//                SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
//                        new SSLContextBuilder()
//                                .loadTrustMaterial(keyStore, new TrustSelfSignedStrategy())
//                                .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
//                                .build(),
//                        NoopHostnameVerifier.INSTANCE);
//
//                HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(
//                        socketFactory).build();
//
//                ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
//                        httpClient);
//                RestTemplate restTemplate = new RestTemplate(requestFactory);
                items = restTemplate.getForObject(new URI("https://localhost:8085/api/item"), List.class);
            } else {
                items = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            items = null;
        }
        return items;
    }

    public Item getItem(String itemId) {
        Item item;
        try {
            item = restTemplate.getForObject(new URI("https://localhost:8085/api/item/") + itemId, Item.class);
        } catch (Exception e) {
            item = null;
        }
        return item;
    }
}
