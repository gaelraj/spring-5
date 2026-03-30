package com.restaurant.td5springboot.service;

import com.restaurant.td5springboot.entity.Dish;
import com.restaurant.td5springboot.entity.Ingredient;
import com.restaurant.td5springboot.repository.DishRepository;
import com.restaurant.td5springboot.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Dish updateIngredients(int dishId, List<Ingredient> ingredients) {

        dishRepository.findById(dishId);
        System.out.println(ingredients);
        List<Integer> ids = ingredients.stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());
        System.out.println(ids);
        List<Ingredient> existingIngredients = ingredientRepository.findAllByIds(ids);

        return dishRepository.updateIngredients(dishId, existingIngredients);
    }

}
