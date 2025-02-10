package org.ptithcm2021.fashionshop.repository;

import org.ptithcm2021.fashionshop.model.Brand;
import org.ptithcm2021.fashionshop.model.Category;
import org.ptithcm2021.fashionshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<List<Product>> findByBrandAndCategory(Brand Brand, Category Category);
}
