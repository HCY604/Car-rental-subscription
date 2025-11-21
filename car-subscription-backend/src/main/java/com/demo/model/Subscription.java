package com.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 指向 cars 表
    @Column(name = "car_id")
    private Integer carId;

    // 預留給會員（未來 JWT）
    @Column(name = "member_id")
    private Integer memberId;

    private String store;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "start_time")
    private String startTime;

    private Integer months;

    @Column(name = "mileage_bonus")
    private Integer mileageBonus;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "final_price")
    private Integer finalPrice;
}
