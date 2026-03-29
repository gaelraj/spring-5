package com.restaurant.td5springboot.controller;

import com.restaurant.td5springboot.entity.Ingredient;
import com.restaurant.td5springboot.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;


    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ingredientService.getAllIngredients());
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int idIngredient) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ingredientService.getIngredientById(idIngredient));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



}
