package com.restaurant.td5springboot.controller;

import com.restaurant.td5springboot.entity.Dish;
import com.restaurant.td5springboot.entity.Ingredient;
import com.restaurant.td5springboot.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/dishes/{id}/ingredients")
    public ResponseEntity<?> getIngredientsByDishId(
            @PathVariable int id,
            @RequestParam(required = false) String ingredientName,
            @RequestParam(required = false) Double ingredientPriceAround) {

        try {
            List<Ingredient> ingredients = dishService.getIngredientsByDishId(id, ingredientName, ingredientPriceAround);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ingredients);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/dishes/{id}/ingredients")
    public ResponseEntity<?> updateIngredients(
            @PathVariable int id,
            @RequestBody(required = false) List<Ingredient> ingredients) {

        if (ingredients == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Request body is required");
        }

        try {
            Dish dish = dishService.updateIngredients(id, ingredients);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dish);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

}
