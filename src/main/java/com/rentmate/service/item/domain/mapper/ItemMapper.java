package com.rentmate.service.item.domain.mapper;

import com.rentmate.service.item.domain.dto.ItemRequestDTO;
import com.rentmate.service.item.domain.dto.ItemResponseDTO;
import com.rentmate.service.item.domain.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ItemResponseDTO itemToItemResponseDTO(Item item);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "availability", ignore = true)
    Item itemRequestDTOToItem(ItemRequestDTO itemRequestDTO);

    @Mapping(target = "category", ignore = true)
    void updateItemFromDTO(ItemRequestDTO itemRequestDTO, @MappingTarget Item item);

    @Mapping(source = "category.id", target = "categoryId")
    ItemResponseDTO updateAvailabilityToDTO(Item item);
}