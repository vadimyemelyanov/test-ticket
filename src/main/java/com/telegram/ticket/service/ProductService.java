package com.telegram.ticket.service;

import com.telegram.ticket.domain.Products;
import com.telegram.ticket.repository.ProductsRepository;
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
