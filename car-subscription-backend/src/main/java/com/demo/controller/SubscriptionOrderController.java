package com.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.model.SubscriptionOrder;
import com.demo.repository.SubscriptionOrderRepository;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class SubscriptionOrderController {

    @Autowired
    private SubscriptionOrderRepository repo;

    // 自動產生訂單編號
    private String generateOrderNo() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long count = repo.count() + 1;
        return "O" + date + String.format("%04d", count);
    }

    // ⭐ 新增訂單：含三項邏輯
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SubscriptionOrder order) {
    	
    	if (order.getMemberId() == null || order.getMemberId().isEmpty()) {
    	        return ResponseEntity
    	                .status(HttpStatus.UNAUTHORIZED)
    	                .body("請先登入會員才能訂閱服務");
    	    }
    	

        // 1️⃣ 欄位不能空白
        if (order.getMemberId() == null || 
            order.getCarId() == null ||
            order.getStore() == null || order.getStore().isEmpty() ||
            order.getStartDate() == null || order.getStartDate().isEmpty() ||
            order.getStartTime() == null || order.getStartTime().isEmpty() ||
            order.getMonths() == null) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("欄位不能為空白，請完整填寫訂單資料");
        }

        // 2️⃣ 防止重複訂閱（同會員 + 同車種）
        if (repo.existsByMemberIdAndCarId(order.getMemberId(), order.getCarId())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("你已經訂閱過該車種，不能重複訂閱");
        }

        // 3️⃣ 自動產生訂單編號
        order.setOrderNo(generateOrderNo());

        SubscriptionOrder saved = repo.save(order);
        return ResponseEntity.ok(saved);
    }

    // 查所有
    @GetMapping
    public List<SubscriptionOrder> getAll() {
        return repo.findAll();
    }

    // 查會員自己的訂單
    @GetMapping("/member/{memberId}")
    public List<SubscriptionOrder> getByMember(@PathVariable String memberId) {
        return repo.findByMemberId(memberId);
    }
}
