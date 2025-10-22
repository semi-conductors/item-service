package com.rentmate.service.item.domain.dto;

import lombok.Data;

@Data
public class ItemResponseDTO {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private Long categoryId;
    private String imageUrl;
    private boolean availability;
    private String ownerAddress;
}
