package com.rockstarinc.RIecom.services.admin.category;

import java.util.List;

import com.rockstarinc.RIecom.dto.CategoryDto;
import com.rockstarinc.RIecom.entity.Category;

public interface CategoryService {

    Category createCategory(CategoryDto categoryDto);

    List<Category> getAllCategories();
}
