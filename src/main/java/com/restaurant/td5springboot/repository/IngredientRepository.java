package com.restaurant.td5springboot.repository;

import com.restaurant.td5springboot.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;
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

    public StockValue findStockValueAt(int idIngredient, Instant at) {
        String sql = """
                select unit,
                sum(
                    case 
                        when type = 'IN' then quantity
                        else -quantity
                    end 
                ) as current_stock
                from stockmovement
                where id_ingredient = ?
                    and creation_datetime <= ?
                group by unit
                """;

        return jdbcTemplate.queryForObject(sql,(rs, rowNum) -> {
            StockValue stockValue = new StockValue();
            stockValue.setQuantity(rs.getDouble("current_stock"));
            stockValue.setUnit(Unit.valueOf(rs.getString("unit")));
            return stockValue;
        }, idIngredient, Timestamp.from(at));
    }

    public List<Ingredient> findAllByIds(List<Integer> ids) {
        String sql = """
            SELECT id, name, price, category
            FROM Ingredient
            WHERE id = ANY(?)
            """;

        Array array = jdbcTemplate.execute((Connection con) ->
                con.createArrayOf("integer",
                        ids.toArray()));

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                CategoryEnum.valueOf(rs.getString("category"))
        ), array);
    }

}
