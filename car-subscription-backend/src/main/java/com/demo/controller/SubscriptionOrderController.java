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
@CrossOrigin(origins = "*")//@CrossOrigin最後要刪
public class SubscriptionOrderController {

    @Autowired
    private SubscriptionOrderRepository repo;

    // 自動產生訂單編號
    private String generateOrderNo() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long count = repo.count() + 1;
        return "O" + date + String.format("%04d", count);
    }

    // 新增訂單（含登入驗證 / 空白欄位 / 重複訂閱）
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SubscriptionOrder order) {

        // 必須登入
        if (order.getMemberId() == null || order.getMemberId().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("請先登入會員才能訂閱服務");
        }

        // 必填欄位檢查
        if (order.getCarId() == null ||
            order.getStore() == null || order.getStore().isEmpty() ||
            order.getStartDate() == null || order.getStartDate().isEmpty() ||
            order.getStartTime() == null || order.getStartTime().isEmpty() ||
            order.getMonths() == null) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("欄位不能為空白，請完整填寫訂單資料");
        }

        // 防止重複訂閱
        if (repo.existsByMemberIdAndCarId(order.getMemberId(), order.getCarId())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("你已經訂閱過該車種，不能重複訂閱");
        }

        // 自動產生訂單編號
        order.setOrderNo(generateOrderNo());

        // 自動判斷訂單狀態（進行中 / 已完成）
        autoSetOrderStatus(order);

        SubscriptionOrder saved = repo.save(order);
        return ResponseEntity.ok(saved);
    }

    // 自動設定訂單狀態邏輯
    private void autoSetOrderStatus(SubscriptionOrder o) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(o.getStartDate());
            Date end = new Date(start.getTime() + (long)o.getMonths() * 30 * 24 * 60 * 60 * 1000);
            Date now = new Date();

            if (now.before(end)) {
                o.setStatus("進行中");
            } else {
                o.setStatus("已完成");
            }
        } catch (Exception e) {
            o.setStatus("進行中");
        }
    }

    // 查所有訂單
    @GetMapping
    public List<SubscriptionOrder> getAll() {
        List<SubscriptionOrder> list = repo.findAll();
        list.forEach(this::autoSetOrderStatus);
        return repo.saveAll(list);
    }

    // 查會員訂單（不含狀態更新）
    @GetMapping("/member/{memberId}")
    public List<SubscriptionOrder> getByMember(@PathVariable String memberId) {
        return repo.findByMemberId(memberId);
    }

    // 查會員訂單（含自動更新狀態給前端訂單查詢頁使用
    @GetMapping("/member/{memberId}/status")
    public List<SubscriptionOrder> getByMemberWithStatus(@PathVariable String memberId) {

        List<SubscriptionOrder> list = repo.findByMemberId(memberId);

        // 自動更新每筆訂單狀態
        list.forEach(this::autoSetOrderStatus);

        return repo.saveAll(list);
    }
}
