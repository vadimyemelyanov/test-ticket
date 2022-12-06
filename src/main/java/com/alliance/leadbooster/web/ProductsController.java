package com.alliance.leadbooster.web;

import com.alliance.leadbooster.persistence.entity.Products;
import com.alliance.leadbooster.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;


    @GetMapping
    public List<Products> getProductNames() {
        return productService.getAvailableLabels();
    }

}
