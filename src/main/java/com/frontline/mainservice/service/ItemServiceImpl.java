package com.frontline.mainservice.service;

import com.frontline.mainservice.model.Item;

import java.util.List;

public interface ItemServiceImpl {
    public List<Item> getItems();
    public Item getItem(String itemId);
}
