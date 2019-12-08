package com.frontline.mainservice.service;

import com.frontline.mainservice.config.Config;
import com.frontline.mainservice.controller.CustomResponse;
import com.frontline.mainservice.model.Item;
import com.frontline.mainservice.utils.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class ItemService {
    public static Logger logger = Logger.getLogger(ItemService.class.toString());

    @Autowired
    RestTemplate restTemplate;

    public CustomResponse getItems() {
        StateArray stateArray = new StateArray();
        return queryURL_demo(Config.GET_ALL_CATALOGUE_ITEMS_URL, HttpMethod.GET, null, "List", stateArray);
    }

    public CustomResponse getItem(java.lang.String itemId) {
        return queryURL(Config.GET_ONE_CATALOGUE_ITEMS_URL + itemId, HttpMethod.GET, null, "Item");
    }

    public CustomResponse postItem(Item item) {
        return queryURL(Config.POST_CATALOGUE_ITEM_URL, HttpMethod.POST, item, "Item");
    }


    @Async
    public CompletableFuture getAsync(java.lang.String url, HttpMethod httpMethod, Item requestBody, java.lang.String type) throws InterruptedException, URISyntaxException {
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
            httpEntity = restTemplate.exchange(url, httpMethod, requestEntity, java.lang.String.class);

        }
        // Artificial delay of 1s
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(httpEntity);
    }

    @Async
    public CompletableFuture getAsync_demo(java.lang.String url, HttpMethod httpMethod, Item requestBody, java.lang.String type, StateArray arrayList) throws InterruptedException, URISyntaxException {
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
            httpEntity = restTemplate.exchange(url, httpMethod, requestEntity, java.lang.String.class);

        }
        // Artificial delay of 1s
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(httpEntity);
    }


    public CustomResponse queryURL(java.lang.String url, HttpMethod httpMethod, Item requestBody, java.lang.String type) {
        Object responseBody = null;
        CustomResponse customResponse = new CustomResponse();
        try {
            logger.info("Requesting data from " + url);
            CompletableFuture completableFuture = this.getAsync(url, httpMethod, requestBody, type);
            HttpEntity httpEntity;
            if (completableFuture.isDone()) {
                httpEntity = (HttpEntity) this.getAsync(url, httpMethod, requestBody, type).get();
            } else {
                httpEntity = null;
            }
            HttpHeaders httpHeaders = httpEntity.getHeaders();
            boolean serverCertValid = MSchainCertHandler.validateServerCert(httpHeaders);
            if (!serverCertValid) {
                responseBody = new CertInvalidException("Server Certificate is Invalid").getMessage();
                customResponse.setMessage(Config.REQUEST_FAILED_MSG);
                customResponse.setStatus(Config.BAD_REQUEST);
            }

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


    public CustomResponse queryURL_demo(java.lang.String url, HttpMethod httpMethod, Item requestBody, java.lang.String type, StateArray stateArray) {
        Object responseBody = null;
        CustomResponse customResponse = new CustomResponse();
        try {
            logger.info("Requesting data from " + url);
            CompletableFuture completableFuture = null;
            try {
                completableFuture = this.getAsync_demo(url, httpMethod, requestBody, type, stateArray);
                stateArray.sendRequestToServer.setStateMessage(StateMessage.SUCCESS);
                stateArray.establishTLSConnection.setStateMessage(StateMessage.SUCCESS);

            } catch (Exception e) {
                stateArray.sendRequestToServer.setStateMessage(StateMessage.FAILED);
                stateArray.establishTLSConnection.setStateMessage(StateMessage.FAILED);
                stateArray.receivedServerResponse.setStateMessage(StateMessage.FAILED);
                stateArray.sendCertToMSchain.setStateMessage(StateMessage.FAILED);
                stateArray.receivedMSChainResponse.setStateMessage(StateMessage.FAILED);
                stateArray.validateServerCertificate.setStateMessage(StateMessage.FAILED);
            }
            HttpEntity httpEntity;
            try {
                httpEntity = (HttpEntity) completableFuture.get();
                stateArray.receivedServerResponse.setStateMessage(StateMessage.SUCCESS);

                HttpHeaders httpHeaders = httpEntity.getHeaders();
                boolean serverCertValid = MSchainCertHandler.validateServerCert_demo(httpHeaders, stateArray);


                if (!serverCertValid) {
                    responseBody = new CertInvalidException("Server Certificate is Invalid").getMessage();
                    customResponse.setMessage(Config.REQUEST_FAILED_MSG);
                    customResponse.setStatus(Config.BAD_REQUEST);
                }

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
            } catch (Exception e) {
                stateArray.receivedServerResponse.setStateMessage(StateMessage.FAILED);
                stateArray.sendCertToMSchain.setStateMessage(StateMessage.FAILED);
                stateArray.receivedMSChainResponse.setStateMessage(StateMessage.FAILED);
                stateArray.validateServerCertificate.setStateMessage(StateMessage.FAILED);
            }

        } catch (Exception e) {
            logger.info("ERROR!!!");
            logger.info("Exception : " + e.getMessage());
            responseBody = e.getMessage();
            e.printStackTrace();
            customResponse.setStatus(Config.BAD_REQUEST);
            customResponse.setData(stateArray);
            customResponse.setMessage(Config.REQUEST_FAILED_MSG);
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("states", stateArray);
        data.put("data", responseBody);
        customResponse.setData(data);
        return customResponse;
    }
}
