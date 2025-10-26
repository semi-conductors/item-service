package com.rentmate.service.item.domain.dto;

import lombok.Data;

@Data
public class ItemResponseDTO {

    private Long id;
    private Long ownerId;
    private String title;
    private String description;
    private Double rentalPrice;
    private Long categoryId;
    private String imageUrl;
    private boolean availability;
    private String ownerAddress;
    ////////////////////////
//    private String ownerName;
//    private Double rating;
//    private Integer totalRatings;
}
