package com.restaurant.td5springboot.repository;

import com.restaurant.td5springboot.entity.CategoryEnum;
import com.restaurant.td5springboot.entity.Dish;
import com.restaurant.td5springboot.entity.DishTypeEnum;
import com.restaurant.td5springboot.entity.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DishRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Dish> findAll() {
        String sql = """
                SELECT d.id as dish_id, d.name as dish_name, d.price, d.dish_type,
                       i.id as ingredient_id, i.name as ingredient_name,
                       i.category, i.price as ingredient_price
                FROM Dish d
                LEFT JOIN DishIngredient di ON d.id = di.id_dish
                LEFT JOIN Ingredient i ON di.id_ingredient = i.id
                ORDER BY d.id
                """;

        Map<Integer, Dish> dishMap = new LinkedHashMap<>();

        jdbcTemplate.query(sql, (rs, rowNum) -> {
            int dishId = rs.getInt("dish_id");


            dishMap.putIfAbsent(dishId, new Dish(
                    dishId,
                    rs.getString("dish_name"),
                    DishTypeEnum.valueOf(rs.getString("dish_type")),
                    rs.getDouble("price")
            ));

            int ingredientId = rs.getInt("ingredient_id");

            if (ingredientId != 0) {
                Ingredient ingredient = new Ingredient(
                        ingredientId,
                        rs.getString("ingredient_name"),
                        rs.getDouble("ingredient_price"),
                        CategoryEnum.valueOf(rs.getString("category"))
                );
                dishMap.get(dishId).getIngredients().add(ingredient);
            }

            return null;
        });

        return new ArrayList<>(dishMap.values());
    }

    public Dish findById(int id) {

        String sql = """
            SELECT d.id as dish_id, d.name as dish_name, d.price, d.dish_type
            FROM Dish d
            WHERE d.id = ?
            """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new Dish(
                            rs.getInt("dish_id"),
                            rs.getString("dish_name"),
                            DishTypeEnum.valueOf(rs.getString("dish_type")),
                            rs.getDouble("price")
                    ), id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Dish.id=" + id + " is not found");
        }
    }

    public Dish updateIngredients(int dishId, List<Ingredient> ingredients) {

        String deleteSql = "DELETE FROM DishIngredient WHERE id_dish = ?";
        jdbcTemplate.update(deleteSql, dishId);

        String insertSql = """
                INSERT INTO DishIngredient (id_dish, id_ingredient, quantity_required, unit)
                    VALUES (?, ?, ?, ?::unit_type)
            """;

        for (Ingredient ingredient : ingredients) {
            jdbcTemplate.update(insertSql,
                    dishId,
                    ingredient.getId(),
                    0.0,
                    "KG"
            );
        }

        return findById(dishId);
    }

}
