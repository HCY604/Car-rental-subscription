package com.demo.controller;

import com.demo.model.Car;
import com.demo.service.CarService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*")   // 允許前端存取
public class CarController {

    private final CarService service;

    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping
    public List<Car> getAllCars() {
        return service.getAllCars();
    }
}
