package com.restaurant.td5springboot.controller;

import com.restaurant.td5springboot.entity.Dish;
import com.restaurant.td5springboot.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/dishes")
    public ResponseEntity<List<Dish>> getDishes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dishService.getAllDishes());
    }

}
