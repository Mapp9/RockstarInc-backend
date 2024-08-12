package com.rockstarinc.RIecom.services.admin.product;

import java.io.IOException;
import java.util.List;

import com.rockstarinc.RIecom.dto.ProductDto;

public interface AdminProductService {

    ProductDto addProduct(ProductDto productDto) throws IOException;

    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductByName(String name);
}
