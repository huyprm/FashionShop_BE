package org.ptithcm2021.fashionshop.repository;

import org.ptithcm2021.fashionshop.model.Product_variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<Product_variant, Integer> {
}
