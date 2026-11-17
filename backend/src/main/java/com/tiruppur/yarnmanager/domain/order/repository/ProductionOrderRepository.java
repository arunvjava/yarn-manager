package com.tiruppur.yarnmanager.domain.order.repository;

import com.tiruppur.yarnmanager.domain.order.entity.OrderStatus;
import com.tiruppur.yarnmanager.domain.order.entity.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {
    Optional<ProductionOrder> findByOrderNumber(String orderNumber);
    List<ProductionOrder> findByStatus(OrderStatus status);
}
