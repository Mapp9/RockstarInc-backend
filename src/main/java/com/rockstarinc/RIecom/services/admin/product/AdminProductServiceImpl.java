package com.rockstarinc.RIecom.services.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rockstarinc.RIecom.dto.ProductDto;
import com.rockstarinc.RIecom.entity.Category;
import com.rockstarinc.RIecom.entity.Product;
import com.rockstarinc.RIecom.repository.CategoryRepository;
import com.rockstarinc.RIecom.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public ProductDto addProduct(ProductDto productDto) throws IOException {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImg(productDto.getImg().getBytes());

        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();

        product.setCategory(category);
        return productRepository.save(product).getDto();
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).toList();
    }

    @Override
    public List<ProductDto> getAllProductByName(String name) {
        List<Product> products = productRepository.findAllByNameContaining(name);
        return products.stream().map(Product::getDto).toList();
    }
}
