package com.rockstarinc.RIecom.services.admin.category;

import org.springframework.stereotype.Service;

import com.rockstarinc.RIecom.dto.CategoryDto;
import com.rockstarinc.RIecom.entity.Category;
import com.rockstarinc.RIecom.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public Category createCategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        
        return categoryRepository.save(category);
    }
}
