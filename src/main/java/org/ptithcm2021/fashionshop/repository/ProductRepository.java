package org.ptithcm2021.fashionshop.repository;

import org.ptithcm2021.fashionshop.model.Brand;
import org.ptithcm2021.fashionshop.model.Category;
import org.ptithcm2021.fashionshop.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<List<Product>> findByBrandAndCategory(Brand Brand, Category Category);

    @Query(value = "select id, name from products where match(name, description) against (?1 in boolean mode)", nativeQuery = true)
    Map<Integer, String> searchProduct(String keyword);
}
