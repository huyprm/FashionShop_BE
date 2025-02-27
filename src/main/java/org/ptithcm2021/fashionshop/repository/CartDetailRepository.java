package org.ptithcm2021.fashionshop.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.ptithcm2021.fashionshop.model.CartDiscountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDiscountDetail, Integer> {
}
