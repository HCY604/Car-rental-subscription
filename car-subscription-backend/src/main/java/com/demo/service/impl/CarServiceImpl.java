package com.demo.service.impl;

import com.demo.model.Car;
import com.demo.repository.CarRepository;
import com.demo.service.CarService;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository repo;

    public CarServiceImpl(CarRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Car> getAllCars() {
        return repo.findAll();
    }
}
