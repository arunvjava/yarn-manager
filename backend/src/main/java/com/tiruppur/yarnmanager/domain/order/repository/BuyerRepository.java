package com.tiruppur.yarnmanager.domain.order.repository;

import com.tiruppur.yarnmanager.domain.order.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    Optional<Buyer> findByBuyerCode(String buyerCode);
    List<Buyer> findByActiveTrue();
    boolean existsByBuyerCode(String buyerCode);
}
