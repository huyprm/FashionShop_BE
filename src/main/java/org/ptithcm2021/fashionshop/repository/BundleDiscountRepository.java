package org.ptithcm2021.fashionshop.repository;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.model.BundleDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BundleDiscountRepository extends JpaRepository<BundleDiscount, Integer> {
}
