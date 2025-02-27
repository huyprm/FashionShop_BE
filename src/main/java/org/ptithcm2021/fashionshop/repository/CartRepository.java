package org.ptithcm2021.fashionshop.repository;

import org.ptithcm2021.fashionshop.model.Cart;
import org.ptithcm2021.fashionshop.model.CartDiscountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserIdAndProductVariantId(String userId, int productVariantId);
    Optional<List<Cart>> findByUserId(String userId);
}
