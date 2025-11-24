package com.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.demo.model.SubscriptionOrder;
import com.demo.service.SubscriptionOrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class SubscriptionOrderController {

    private final SubscriptionOrderService service;

    public SubscriptionOrderController(SubscriptionOrderService service) {
        this.service = service;
    }

    // C
    @PostMapping
    public SubscriptionOrder createOrder(@RequestBody SubscriptionOrder order) {
        String orderNo = "SO-" + System.currentTimeMillis(); // 自動產生訂單編號
        order.setOrderNo(orderNo);
        return service.createOrder(order);
    }

    // R
    @GetMapping
    public List<SubscriptionOrder> getAllOrders() {
        return service.getAllOrders();
    }

    // R
    @GetMapping("/{id}")
    public SubscriptionOrder getOrderById(@PathVariable Integer id) {
        return service.getOrderById(id);
    }

    // U
    @PutMapping("/{id}")
    public SubscriptionOrder updateOrder(
            @PathVariable Integer id,
            @RequestBody SubscriptionOrder data) {
        return service.updateOrder(id, data);
    }

    // D
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        service.deleteOrder(id);
        return "訂單已刪除 id=" + id;
    }
}
