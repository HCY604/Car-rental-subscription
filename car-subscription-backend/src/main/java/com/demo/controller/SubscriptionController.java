package com.demo.controller;

import com.demo.model.Subscription;
import com.demo.service.SubscriptionService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscribe")
@CrossOrigin(origins = "*")   // 讓前端可以POST JSON
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    /** 
     * Step3 最後傳訂閱資料
     */
    @PostMapping
    public Subscription createOrder(@RequestBody Subscription order) {

        // 預留JWT功能，先不驗證 Token
        // 之後可以在這裡塞memberId

        return service.saveOrder(order);
    }
}
