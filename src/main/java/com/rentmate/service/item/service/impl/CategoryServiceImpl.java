package com.rentmate.service.item.service.impl;

import com.rentmate.service.item.domain.dto.CategoryDTO;
import com.rentmate.service.item.domain.entity.Category;
import com.rentmate.service.item.domain.mapper.CategoryMapper;
import com.rentmate.service.item.exception.CategoryAlreadyExistsException;
import com.rentmate.service.item.exception.CategoryNotFoundException;
import com.rentmate.service.item.repository.CategoryRepository;
import com.rentmate.service.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new CategoryAlreadyExistsException(categoryDTO.getName());
        }
        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        if (categoryRepository.existsByName(categoryDTO.getName()) || !category.getName().equals(categoryDTO.getName())) {
            throw new CategoryAlreadyExistsException(categoryDTO.getName());
        }
        category.setName(categoryDTO.getName());
        category = categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMapper.toDTO(category);
    }
}
