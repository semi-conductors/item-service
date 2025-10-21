package com.rentmate.service.item.service;

import com.rentmate.service.item.domain.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
}
