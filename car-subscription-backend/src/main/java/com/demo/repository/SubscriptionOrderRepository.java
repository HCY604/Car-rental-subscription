package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.model.SubscriptionOrder;

public interface SubscriptionOrderRepository extends JpaRepository<SubscriptionOrder, Integer> {
}
