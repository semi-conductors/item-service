package com.rentmate.service.item.service.impl;

import com.rentmate.service.item.client.UserClient;
import com.rentmate.service.item.domain.dto.ItemRequestDTO;
import com.rentmate.service.item.domain.dto.ItemResponseDTO;
import com.rentmate.service.item.domain.dto.UserResponseDTO;
import com.rentmate.service.item.domain.entity.Category;
import com.rentmate.service.item.domain.entity.Item;
import com.rentmate.service.item.domain.mapper.ItemMapper;
import com.rentmate.service.item.exception.CategoryNotFoundException;
import com.rentmate.service.item.exception.ItemNotFoundException;
import com.rentmate.service.item.exception.OwnerMismatchException;
import com.rentmate.service.item.repository.CategoryRepository;
import com.rentmate.service.item.repository.ItemRepository;
import com.rentmate.service.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemMapper itemMapper;
    private final UserClient userClient;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository, ItemMapper itemMapper, UserClient userClient) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemMapper = itemMapper;
        this.userClient = userClient;
    }

    @Override
    public Item createItem(String token, Long ownerId, String title, String description, Double price, Long categoryId, String imageUrl, String ownerAddress) {

        UserResponseDTO user = userClient.getUserById(ownerId, token);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + ownerId);
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow( () -> new CategoryNotFoundException(categoryId));

        Item item = itemMapper.itemRequestDTOToItem(new ItemRequestDTO());
        item.setOwnerId(ownerId);
        item.setCategory(category);
        item.setTitle(title);
        item.setDescription(description);
        item.setPrice(price);
        item.setImageUrl(imageUrl);
        item.setOwnerAddress(ownerAddress);
        item.setAvailability(true);
        item.setIsActive(true);

        return itemRepository.save(item);
    }


    @Override
    public Item updateItem(Long id, Long ownerId, String title, String description, Double price, Long categoryId, String imageUrl, String ownerAddress) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        if (!item.getOwnerId().equals(ownerId)) {
            throw new OwnerMismatchException();
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        ItemRequestDTO itemRequestDTO = new ItemRequestDTO();
        itemRequestDTO.setOwnerId(item.getOwnerId());
        itemRequestDTO.setTitle(title);
        itemRequestDTO.setDescription(description);
        itemRequestDTO.setPrice(price);
        itemRequestDTO.setCategoryId(categoryId);
        itemRequestDTO.setImageUrl(imageUrl);
        itemRequestDTO.setOwnerAddress(ownerAddress);

        itemMapper.updateItemFromDTO(itemRequestDTO, item);
        item.setCategory(category);

        return itemRepository.save(item);
    }

    @Override
    public void softDelete(Long id, Long ownerId, Boolean isActive) {
//        Item item = itemRepository.findById(id)
//                .orElseThrow(() -> new ItemNotFoundException(id));
//
//        if (!item.getOwnerId().equals(ownerId)) {
//            throw new OwnerMismatchException();
//        }
//        item.setIsActive(isActive);
//        itemRepository.save(item);
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        if (!item.getOwnerId().equals(ownerId)) {
            throw new OwnerMismatchException();
        }

        itemRepository.delete(item);
    }

    @Override
    public Boolean isItemAvailable(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        return item.getAvailability();
    }

    @Override
    public Item updateAvailability(Long id, Long ownerId, Boolean availability) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        if (!item.getOwnerId().equals(ownerId)) {
            throw new OwnerMismatchException();
        }

        item.setAvailability(availability);

        return itemRepository.save(item);
    }


    @Override
    public List<Item> getItemsByOwner(Long ownerId) {
        return itemRepository.findByOwnerIdAndIsActiveTrue(ownerId);
    }

//    @Override
//    public Optional<Item> getItemById(Long id) {
//        return itemRepository.findById(id);
//    }
    @Override
    public ItemResponseDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        ItemResponseDTO dto = itemMapper.itemToItemResponseDTO(item);
//        UserResponseDTO owner = userClient.getUserById(item.getOwnerId());
//        if (owner != null) {
//            dto.setOwnerName(owner.getUserName());
//            dto.setRating(owner.getRating());
//            dto.setTotalRatings(owner.getTotalRatings());
//        }
        return dto;
    }

    @Override
    public Page<ItemResponseDTO> searchItems(Long categoryId, Double minPrice, Double maxPrice, String keyword, Pageable pageable) {
        Page<Item> itemsPage;

        if (categoryId != null && minPrice != null && maxPrice != null) {
            itemsPage = itemRepository.findByCategoryIdAndPriceBetweenAndIsActiveTrue(categoryId, minPrice, maxPrice, pageable);

        } else if (categoryId != null) {
            itemsPage = itemRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);

        } else if (minPrice != null && maxPrice != null) {
            itemsPage = itemRepository.findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice, pageable);

        } else if (keyword != null && !keyword.isEmpty()) {
            itemsPage = itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndIsActiveTrue(keyword, keyword, pageable);

        } else {
            itemsPage = itemRepository.findAll(pageable);
        }


        return itemsPage.map(itemMapper::itemToItemResponseDTO);
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
