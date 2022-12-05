package com.alliance.leadbooster.service;

import com.alliance.leadbooster.persistence.entity.Products;
import com.alliance.leadbooster.persistence.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductsRepository productsRepository;


    public List<Products> getAvailableLabels() {
        return productsRepository.findAll();
    }
}
