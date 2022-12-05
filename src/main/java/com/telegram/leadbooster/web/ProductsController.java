package com.telegram.leadbooster.web;

import com.telegram.leadbooster.domain.Products;
import com.telegram.leadbooster.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Products> getProductNames() {
        return productService.getAvailableLabels();
    }

}
