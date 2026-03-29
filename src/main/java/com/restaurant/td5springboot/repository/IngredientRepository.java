package com.restaurant.td5springboot.repository;

import com.restaurant.td5springboot.entity.CategoryEnum;
import com.restaurant.td5springboot.entity.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public class IngredientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Ingredient> findAll() {
        String sql = """
                SELECT id, name, price, category
                FROM Ingredient
                ORDER BY id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ingredient ingredient = new Ingredient();

            ingredient.setId(rs.getInt("id"));
            ingredient.setName(rs.getString("name"));
            ingredient.setPrice(rs.getDouble("price"));
            ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));

            return ingredient;
        });
    }

    public Ingredient findIngredientById(int idIngredient) {
        String sql = """
                SELECT id, name, price, category
                FROM Ingredient
                WHERE id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Ingredient ingredient = new Ingredient();

                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setPrice(rs.getDouble("price"));
                ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));

                return ingredient;
            }, idIngredient );
        } catch (DataAccessException e) {
            throw new RuntimeException("Ingredient.id=" + idIngredient + " is not found");
        }
    }

}
