package com.telegram.leadbooster.service;

import com.telegram.leadbooster.domain.Products;
import com.telegram.leadbooster.repository.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductsRepository productsRepository;

    public ProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Products> getAvailableLabels() {
        return productsRepository.findAll();
    }
}
