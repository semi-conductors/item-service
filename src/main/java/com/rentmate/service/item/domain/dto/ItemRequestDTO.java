package com.rentmate.service.item.domain.dto;

import lombok.Data;

@Data
public class ItemRequestDTO {

    private Long ownerId;
    private String title;
    private String description;
    private Double price;
    private Long categoryId;
    private String imageUrl;
}
