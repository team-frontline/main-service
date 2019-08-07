package com.frontline.mainservice.service;

import com.frontline.mainservice.model.Rating;
import org.json.JSONException;

public interface RatingServiceImpl {
    public Rating rateItem(String productId, Integer rating) throws JSONException;
}
