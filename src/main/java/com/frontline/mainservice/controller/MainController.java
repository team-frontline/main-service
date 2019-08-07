package com.frontline.mainservice.controller;

import com.frontline.mainservice.model.Item;
import com.frontline.mainservice.model.Payment;
import com.frontline.mainservice.model.Rating;
import com.frontline.mainservice.service.ItemServiceImpl;
import com.frontline.mainservice.service.PaymentServiceImpl;
import com.frontline.mainservice.service.RatingServiceImpl;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private RatingServiceImpl ratingService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @GetMapping("/items")
    public List<Item> getItems() {
        List<Item> items = itemService.getItems();
        return items;
    }

    @GetMapping("/item/{itemId}")
    public Item getItem(@PathVariable("itemId") String itemId) {
        return itemService.getItem(itemId);
    }

    @PostMapping("/buy")
    public Payment buyItem(@RequestBody Item itemToBeBought) throws JSONException {
        String itemId = itemToBeBought.getItemID();
        Integer quantity = itemToBeBought.getQuantity();
        double price = itemToBeBought.getPrice();
        return paymentService.buyItem(itemId, quantity, price);
    }

    @PostMapping("/rate")
    public Rating rateItem(@RequestBody Rating customerRating) throws JSONException {
        return ratingService.rateItem(customerRating.getProductId(),customerRating.getRating());
    }
}
