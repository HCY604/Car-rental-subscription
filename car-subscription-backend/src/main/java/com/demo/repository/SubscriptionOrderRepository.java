package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.model.SubscriptionOrder;

import java.util.List;


public interface SubscriptionOrderRepository extends JpaRepository<SubscriptionOrder, Integer> {
    List<SubscriptionOrder> findByMemberId(String memberId);
    
    boolean existsByMemberIdAndCarId(String memberId, Integer carId);
}