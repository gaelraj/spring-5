package com.restaurant.td5springboot.service;

import com.restaurant.td5springboot.entity.Ingredient;
import com.restaurant.td5springboot.entity.MovementTypeEnum;
import com.restaurant.td5springboot.entity.StockValue;
import com.restaurant.td5springboot.entity.Unit;
import com.restaurant.td5springboot.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

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

    public StockValue getStockIngredientByIdAt(int idIngredient, String at, String  unit) {
        Instant date = Instant.parse(at);
        Unit movementType = Unit.valueOf(unit);

        ingredientRepository.findIngredientById(idIngredient);

        StockValue stockValue = ingredientRepository.findStockValueAt(idIngredient, date);
        stockValue.setUnit(movementType);

        return ingredientRepository.findStockValueAt(idIngredient, date);
    }
}
