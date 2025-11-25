package com.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.demo.model.SubscriptionOrder;
import com.demo.repository.SubscriptionOrderRepository;

@Service
public class SubscriptionOrderService {

    private final SubscriptionOrderRepository repo;

    public SubscriptionOrderService(SubscriptionOrderRepository repo) {
        this.repo = repo;
    }

    // C
    public SubscriptionOrder createOrder(SubscriptionOrder order) {
        return repo.save(order);
    }

    // R
    public List<SubscriptionOrder> getAllOrders() {
        return repo.findAll();
    }

    // R
    public SubscriptionOrder getOrderById(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("訂單不存在 id=" + id));
    }

    // U
    public SubscriptionOrder updateOrder(Integer id, SubscriptionOrder newData) {
        SubscriptionOrder order = getOrderById(id);

        order.setStore(newData.getStore());
        order.setStartDate(newData.getStartDate());
        order.setStartTime(newData.getStartTime());
        order.setMonths(newData.getMonths());
        order.setMileageBonus(newData.getMileageBonus());
        order.setTotalPrice(newData.getTotalPrice());
        order.setFinalPrice(newData.getFinalPrice());

        return repo.save(order);
    }

    // D
    public void deleteOrder(Integer id) {
        repo.deleteById(id);
    }
}
