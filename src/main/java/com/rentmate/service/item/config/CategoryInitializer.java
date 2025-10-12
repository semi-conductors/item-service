package com.rentmate.service.item.config;

import com.rentmate.service.item.domain.entity.Category;
import com.rentmate.service.item.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoryInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> categories = Arrays.asList(
                "Electronics",
                "Furniture",
                "Vehicles",
                "Clothing & Accessories",
                "Real Estate",
                "Sports & Fitness",
                "Tools & Equipment",
                "Home Appliances",
                "Party & Events",
                "Books & Education",
                "Other"
        );

        for (String name : categories) {
            if (categoryRepository.findByName(name).isEmpty()) {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
                System.out.println("Added new category: " + name);
            } else {
                System.out.println("Category already exists: " + name);
            }
        }
        System.out.println("Category initialization completed!");
    }
}