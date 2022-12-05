package com.alliance.leadbooster.service;

import com.alliance.leadbooster.persistence.entity.Products;
import com.alliance.leadbooster.persistence.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductsRepository productsRepository;


    @Transactional(readOnly = true)
    public List<Products> getAvailableLabels() {
        return productsRepository.findAll();
    }
}
