package com.rentmate.service.item.domain.mapper;

import com.rentmate.service.item.domain.dto.CategoryDTO;
import com.rentmate.service.item.domain.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
    Category toEntity(CategoryDTO categoryDTO);
}
