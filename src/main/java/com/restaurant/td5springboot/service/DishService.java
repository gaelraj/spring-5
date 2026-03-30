package com.restaurant.td5springboot.service;

import com.restaurant.td5springboot.entity.Dish;
import com.restaurant.td5springboot.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }
}
