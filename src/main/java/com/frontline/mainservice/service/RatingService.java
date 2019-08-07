package com.frontline.mainservice.service;

import com.frontline.mainservice.model.Payment;
import com.frontline.mainservice.model.Rating;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RatingService implements RatingServiceImpl {
    @Override
    public Rating rateItem(String productId, Integer rating) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject ratingObject = new JSONObject();
        ratingObject.put("productId",productId);
        ratingObject.put("rating", rating);

        HttpEntity<String> request = new HttpEntity<String>(ratingObject.toString(), headers);

        Rating newRating = restTemplate.postForObject("http://localhost:8083/update", request, Rating.class);
        return newRating;
    }
}
