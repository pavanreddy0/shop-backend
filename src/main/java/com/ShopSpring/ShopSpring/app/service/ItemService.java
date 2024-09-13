package com.ShopSpring.ShopSpring.app.service;

import com.ShopSpring.ShopSpring.app.dto.Item;
import com.ShopSpring.ShopSpring.app.model.Items;
import com.ShopSpring.ShopSpring.app.model.Users;
import com.ShopSpring.ShopSpring.app.repository.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemsRepository itemsRepository;


    public List<Item> convertItemsToResponse(List<Items> items){
        List<Item> response = new ArrayList<Item>();
        items.forEach(item -> response.add(
                new Item(
                        item.getId(),
                        item.getName(),
                        item.getPrice(),
                        item.getDescription(),
                        item.getDescription(),
                        item.getCount()
                )
        ));
        return response;
    }

    public List<Item> getAllItems(){
        List<Items> items = itemsRepository.findAllByCountGreaterThan(0);
        return convertItemsToResponse(items);
    }


    public List<Item> getAllItemsOfUser(Users users){
        List<Items> items = itemsRepository.findBySeller_Id(users.getId());
        return convertItemsToResponse(items);
    }

    public List<Item> saveItems(Items items){
        itemsRepository.save(items);
        return getAllItems();
    }

    public List<Item> updateItem(Item item){
        Optional<Items> existingItemsOptional = itemsRepository.findById(item.getId());
        if(existingItemsOptional.isPresent()){
            Items existingItems = existingItemsOptional.get();
            existingItems.setPrice(item.getPrice());
            existingItems.setCount(item.getCount());

            itemsRepository.save(existingItems);
            return  getAllItems();
        }
        throw new RuntimeException("Product with ID " + item.getId() + " not found");
    }

    public List<Items> deleteItem(Items items){
        return itemsRepository.deleteById(items);
    }
}
