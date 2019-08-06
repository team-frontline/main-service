package com.frontline.mainservice.service;

import com.frontline.mainservice.model.Item;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ItemService implements ItemServiceImpl {

    @Override
    public List<Item> getItems() {
        RestTemplate restTemplate = new RestTemplate();
        List items = restTemplate.getForObject("http://localhost:8081/api/item", List.class);
        return items;
    }

    @Override
    public Item getItem(String itemId) {
        RestTemplate restTemplate = new RestTemplate();
        Item item = restTemplate.getForObject("http://localhost:8081/api/item/" + itemId, Item.class);
        return item;
    }

    @Override
    public Item buyItem(String itemId, int quantity) {
        //TODO: REST API call to catalogue to pay and buy
        return null;
    }
}
