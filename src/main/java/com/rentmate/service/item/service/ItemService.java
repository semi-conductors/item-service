package com.rentmate.service.item.service;

import com.rentmate.service.item.domain.dto.ItemResponseDTO;
import com.rentmate.service.item.domain.entity.Category;
import com.rentmate.service.item.domain.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item createItem(Long ownerId, String title, String description, Double price, Long categoryId, String imageUrl);
    Item updateItem(Long id, Long ownerId, String title, String description, Double price, Long categoryId, String imageUrl);
    void softDelete(Long id, Long ownerId, Boolean isActive);
    Boolean isItemAvailable(Long id);
    Item updateAvailability(Long id, Long ownerId, Boolean availability);
    List<Item> getItemsByOwner(Long ownerId);
    Optional<Item> getItemById(Long id);
    Page<ItemResponseDTO> searchItems(Long categoryId, Double minPrice, Double maxPrice, String keyword, Pageable pageable);
    List<Category> getAllCategories();
}
