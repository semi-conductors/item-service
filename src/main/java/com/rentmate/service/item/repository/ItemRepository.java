package com.rentmate.service.item.repository;

import com.rentmate.service.item.domain.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerIdAndIsActiveTrue(Long ownerId);

    Page<Item> findByCategoryIdAndPriceBetweenAndIsActiveTrue(
            Long categoryId, Double minPrice, Double maxPrice, Pageable pageable);

    Page<Item> findByPriceBetweenAndIsActiveTrue(Double minPrice, Double maxPrice, Pageable pageable);

    Page<Item> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndIsActiveTrue(
            String titleKeyword, String descriptionKeyword, Pageable pageable);

    Page<Item> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);
}
