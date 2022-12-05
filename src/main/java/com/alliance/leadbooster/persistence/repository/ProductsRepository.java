package com.alliance.leadbooster.persistence.repository;

import com.alliance.leadbooster.persistence.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Products, String> {
}