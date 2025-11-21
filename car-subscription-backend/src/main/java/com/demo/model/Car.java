package com.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String brand;

    private Integer price;

    private String area;

    private String store;

    private String img;   // 例如 img/car1.jpg（與前端相同）
}
