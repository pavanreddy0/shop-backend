package com.ShopSpring.ShopSpring.app.repository;

import com.ShopSpring.ShopSpring.app.model.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Items, Long>, PagingAndSortingRepository<Items, Long>  {
//    get all the items using pagination
    public List<Items> findAllByCountGreaterThan(int count);
    public List<Items> findBySeller_Id(Long sellerId);

//    public List<Items> updateItemsById(Items items);

    public List<Items> deleteById(Items items);

}
