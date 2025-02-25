package org.ptithcm2021.fashionshop.repository;

import org.ptithcm2021.fashionshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
