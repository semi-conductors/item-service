package com.rentmate.service.item.controller;

import com.cloudinary.Cloudinary;
import com.rentmate.service.item.domain.dto.ItemRequestDTO;
import com.rentmate.service.item.domain.dto.ItemResponseDTO;
import com.rentmate.service.item.domain.entity.Category;
import com.rentmate.service.item.domain.entity.Item;
import com.rentmate.service.item.domain.mapper.ItemMapper;
import com.rentmate.service.item.exception.ItemNotFoundException;
import com.rentmate.service.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private Cloudinary cloudinary;

    public ItemController(ItemService itemService, ItemMapper itemMapper, Cloudinary cloudinary) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.cloudinary = cloudinary;
    }

//    @PostMapping
//    public ResponseEntity<ItemResponseDTO> createItem(
//            @RequestPart("imageFile") MultipartFile imageFile,
////            @RequestBody ItemRequestDTO itemRequestDTO,
//            @RequestPart("itemData") ItemRequestDTO itemRequestDTO,
//            @RequestHeader("Authorization") String token){
//
//        String tempImageUrl = "/images/" + imageFile.getOriginalFilename();
//        itemRequestDTO.setImageUrl(tempImageUrl);
//
//        Item item = itemService.createItem(
//                token,
//                itemRequestDTO.getOwnerId(),
//                itemRequestDTO.getTitle(),
//                itemRequestDTO.getDescription(),
//                itemRequestDTO.getPrice(),
//                itemRequestDTO.getCategoryId(),
//                itemRequestDTO.getImageUrl(),
//                itemRequestDTO.getOwnerAddress()
//        );
//        return ResponseEntity.ok(itemMapper.itemToItemResponseDTO(item));
//    }
    @PostMapping
    public ResponseEntity<ItemResponseDTO> createItem(
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestPart("itemData") ItemRequestDTO itemRequestDTO,
            @RequestHeader("Authorization") String token) {

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), Map.of());

            String uploadedImageUrl = (String) uploadResult.get("secure_url");
            itemRequestDTO.setImageUrl(uploadedImageUrl);

            Item item = itemService.createItem(
                    token,
                    itemRequestDTO.getOwnerId(),
                    itemRequestDTO.getTitle(),
                    itemRequestDTO.getDescription(),
                    itemRequestDTO.getPrice(),
                    itemRequestDTO.getCategoryId(),
                    itemRequestDTO.getImageUrl(),
                    itemRequestDTO.getOwnerAddress()
            );

            return ResponseEntity.ok(itemMapper.itemToItemResponseDTO(item));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ItemResponseDTO>> getItemsByOwner(@PathVariable Long ownerId) {
        List<Item> items = itemService.getItemsByOwner(ownerId);
        List<ItemResponseDTO> responseDTOs = items.stream()
                .map(itemMapper::itemToItemResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Long id) {
        ItemResponseDTO item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(
            @PathVariable Long id,
            @RequestBody ItemRequestDTO itemRequestDTO) {
        Item item = itemService.updateItem(
                id,
                itemRequestDTO.getOwnerId(),
                itemRequestDTO.getTitle(),
                itemRequestDTO.getDescription(),
                itemRequestDTO.getPrice(),
                itemRequestDTO.getCategoryId(),
                itemRequestDTO.getImageUrl(),
                itemRequestDTO.getOwnerAddress()
        );
        return ResponseEntity.ok(itemMapper.itemToItemResponseDTO(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id, @RequestParam Long ownerId, @RequestParam(required = false) Boolean isActive) {
        itemService.softDelete(id, ownerId, isActive != null ? isActive : false);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<ItemResponseDTO> updateAvailability(
            @PathVariable Long id,
            @RequestParam Long ownerId,
            @RequestParam Boolean availability) {
        Item item = itemService.updateAvailability(id, ownerId, availability);
        return ResponseEntity.ok(itemMapper.itemToItemResponseDTO(item));
    }

    @GetMapping("/{id}/isAvailabile")
    public ResponseEntity<Boolean> isItemAvailable(@PathVariable Long id) {
        Boolean availability = itemService.isItemAvailable(id);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = itemService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    ///////////////////////////////////////////
    @GetMapping
    public ResponseEntity<Page<ItemResponseDTO>> getAllItems(Pageable pageable) {
        Page<ItemResponseDTO> items = itemService.searchItems(null, null, null, null, pageable);
        return ResponseEntity.ok(items);
    }

    @GetMapping(params = "categoryId")
    public ResponseEntity<Page<ItemResponseDTO>> getItemsByCategory(
            @RequestParam Long categoryId,
            Pageable pageable) {
        Page<ItemResponseDTO> items = itemService.searchItems(categoryId, null, null, null, pageable);
        return ResponseEntity.ok(items);
    }

    @GetMapping(params = "minPrice")
    public ResponseEntity<Page<ItemResponseDTO>> getItemsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        Page<ItemResponseDTO> items = itemService.searchItems(null, minPrice, maxPrice, null, pageable);
        return ResponseEntity.ok(items);
    }

    @GetMapping(params = "keyword")
    public ResponseEntity<Page<ItemResponseDTO>> searchItemsByKeyword(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<ItemResponseDTO> items = itemService.searchItems(null, null, null, keyword, pageable);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemResponseDTO>> searchItemsWithAllFilters(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<ItemResponseDTO> items = itemService.searchItems(categoryId, minPrice, maxPrice, keyword, pageable);
        return ResponseEntity.ok(items);
    }
}
