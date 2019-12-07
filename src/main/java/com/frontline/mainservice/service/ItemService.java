package com.frontline.mainservice.service;

import com.frontline.mainservice.config.Config;
import com.frontline.mainservice.controller.CustomResponse;
import com.frontline.mainservice.model.Item;
import com.frontline.mainservice.utils.CertInvalidException;
import com.frontline.mainservice.utils.MSchainCertHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class ItemService {
    public static Logger logger = Logger.getLogger(ItemService.class.toString());

    @Autowired
    RestTemplate restTemplate;

    public CustomResponse getItems() {
        return queryURL(Config.GET_ALL_CATALOGUE_ITEMS_URL, HttpMethod.GET, null, "List");
    }

    public CustomResponse getItem(String itemId) {
        return queryURL(Config.GET_ONE_CATALOGUE_ITEMS_URL + itemId, HttpMethod.GET, null, "Item");
    }

    public CustomResponse postItem(Item item) {
        return queryURL(Config.POST_CATALOGUE_ITEM_URL, HttpMethod.POST, item, "Item");
    }


    @Async
    public CompletableFuture getAsync(String url, HttpMethod httpMethod, Item requestBody, String type) throws InterruptedException, URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Item> requestEntity = new HttpEntity<>(requestBody, headers);
        HttpEntity httpEntity = null;
        if (type == "List") {
            httpEntity =
                    restTemplate.exchange(url, httpMethod, requestEntity, new ParameterizedTypeReference<List<Item>>() {
                    });
        } else if (type == "Item") {
            httpEntity = restTemplate.exchange(url, httpMethod, requestEntity, Item.class);

        } else {
            httpEntity = restTemplate.exchange(url, httpMethod, requestEntity, String.class);

        }
        // Artificial delay of 1s
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(httpEntity);
    }


    public CustomResponse queryURL(String url, HttpMethod httpMethod, Item requestBody, String type) {
        Object responseBody = null;
        CustomResponse customResponse = new CustomResponse();
        try {
            logger.info("Requesting data from " + url);
            HttpEntity httpEntity = (HttpEntity) this.getAsync(url, httpMethod, requestBody, type).get();
            HttpHeaders httpHeaders = httpEntity.getHeaders();
            if (httpEntity.getBody() != null) {
                if (type.equals("Item")) {
                    responseBody = (Item) httpEntity.getBody();
                } else if (type.equals("List")) {
                    responseBody = (List) httpEntity.getBody();
                }
                customResponse.setMessage(Config.REQUEST_SUCCEED_MSG);
                customResponse.setStatus(Config.SUCCESS);
            } else {
                responseBody = "{}";
                customResponse.setMessage(Config.REQUEST_SUCCEED_MSG);
                customResponse.setStatus(Config.NOT_FOUND);
            }
            boolean serverCertValid = MSchainCertHandler.validateServerCert(httpHeaders);
            if (!serverCertValid) {
                responseBody = new CertInvalidException("Server Certificate is Invalid").getMessage();
                customResponse.setMessage(Config.REQUEST_FAILED_MSG);
                customResponse.setStatus(Config.BAD_REQUEST);
            }

        } catch (Exception e) {
            logger.info("ERROR!!!");
            logger.info("Exception : " + e.getMessage());
            responseBody = e.getMessage();
            e.printStackTrace();
            customResponse.setStatus(Config.BAD_REQUEST);
            customResponse.setMessage(Config.REQUEST_FAILED_MSG);
        }
        customResponse.setData(responseBody);
        return customResponse;
    }
}
