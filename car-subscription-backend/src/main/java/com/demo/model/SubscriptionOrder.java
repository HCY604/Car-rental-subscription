package com.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subscription_order")
public class SubscriptionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_no", unique = true, nullable = false)
    private String orderNo;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "car_id", nullable = false)
    private Integer carId;

    @Column(name = "status")
    private String status;
    
    private String store;
    private String startDate;
    private String startTime;

    private Integer months;
    private Integer mileageBonus;

    private Integer totalPrice;
    private Integer finalPrice;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.sql.Timestamp createdAt;
}
