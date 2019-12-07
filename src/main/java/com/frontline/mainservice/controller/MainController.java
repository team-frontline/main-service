package com.frontline.mainservice.controller;

import com.frontline.mainservice.config.Config;
import com.frontline.mainservice.model.Item;
import com.frontline.mainservice.model.Payment;
import com.frontline.mainservice.model.Rating;
import com.frontline.mainservice.service.ItemService;
import com.frontline.mainservice.service.PaymentServiceImpl;
import com.frontline.mainservice.service.RatingServiceImpl;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("mschain-shopping-cart")
public class MainController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private RatingServiceImpl ratingService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @GetMapping(value = "/health-check")
    public ResponseEntity<CustomResponse> checkHealth() {

        return new ResponseEntity<>(new CustomResponse(Config.REQUEST_SUCCEED_MSG,
                "Working",Config.SUCCESS), HttpStatus.OK);
    }


    @GetMapping(value = "/catalogue/item")
    public ResponseEntity<CustomResponse> getItems() {
        return new ResponseEntity<>( itemService.getItems()
                ,HttpStatus.OK);
    }

    @GetMapping(value = "/catalogue/item/{itemId}")
    public ResponseEntity<CustomResponse> getItem(@PathVariable String itemId) {
        return new ResponseEntity<>(itemService.getItem(itemId)
        ,HttpStatus.OK);
    }

    @PostMapping(value = "/catalogue/item")
    public ResponseEntity<CustomResponse> addItem(@RequestBody Item item){
        return  new ResponseEntity<>(itemService.postItem(item),
                HttpStatus.OK);
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

    @GetMapping("/testme")
    public CustomResponse test(){
        Item i = new Item("t1","Black-linen-trouser",20,2500);
        return itemService.postItem(i);
    }
}
