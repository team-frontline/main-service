package com.frontline.mainservice.controller;

import com.frontline.mainservice.model.Item;
import com.frontline.mainservice.model.Rating;
import com.frontline.mainservice.service.ItemServiceImpl;
import com.frontline.mainservice.service.RatingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private RatingServiceImpl ratingService;

    @GetMapping("/items")
    public List<Item> getItems(){
        List<Item> items = itemService.getItems();
        return items;
    }

    @GetMapping("/item/{itemId}")
    public Item getItem(@PathVariable("itemId") String itemId){
        return itemService.getItem(itemId);
    }

    @PostMapping("/buy")
    public Item buyItem(@RequestBody Item itemToBeBought){
        String itemId = itemToBeBought.getItemID();
        int quantity = itemToBeBought.getQuantity();
        return itemService.buyItem(itemId,quantity);
    }

    @PostMapping("/rate")
    public Rating rateItem(@RequestBody Rating customerRating){
        return ratingService.rateItem(customerRating);
    }
}
