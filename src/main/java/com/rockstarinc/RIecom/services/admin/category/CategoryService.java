package com.rockstarinc.RIecom.services.admin.category;

import com.rockstarinc.RIecom.dto.CategoryDto;
import com.rockstarinc.RIecom.entity.Category;

public interface CategoryService {

    Category createCategory(CategoryDto categoryDto);
}
