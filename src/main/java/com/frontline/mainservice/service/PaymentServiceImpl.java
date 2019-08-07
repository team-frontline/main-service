package com.frontline.mainservice.service;

import com.frontline.mainservice.model.Payment;
import org.json.JSONException;

public interface PaymentServiceImpl {
    public Payment buyItem(String itemId, Integer quantity, double price) throws JSONException;
}
