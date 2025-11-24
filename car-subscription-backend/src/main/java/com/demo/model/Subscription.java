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

    @Column(name = "car_id")
    private Integer carId;

    
    @Column(name = "member_id")
    private String memberId;

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
