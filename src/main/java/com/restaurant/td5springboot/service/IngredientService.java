package com.restaurant.td5springboot.service;

import com.restaurant.td5springboot.entity.Ingredient;
import com.restaurant.td5springboot.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(int idIngredient) {
        return ingredientRepository.findIngredientById(idIngredient);
    }
}
