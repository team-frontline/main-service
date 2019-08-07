package com.frontline.mainservice.service;

import com.frontline.mainservice.model.Item;
import com.frontline.mainservice.model.Payment;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentService implements PaymentServiceImpl {
    @Override
    public Payment buyItem(String itemId, Integer quantity, double price) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject paymentObject = new JSONObject();
        paymentObject.put("itemId",itemId);
        paymentObject.put("quantity", quantity);
        paymentObject.put("price", price);

        HttpEntity<String> request = new HttpEntity<String>(paymentObject.toString(), headers);

        Payment newPayment = restTemplate.postForObject("http://localhost:8082/create", request, Payment.class);
        return newPayment;
    }
}
