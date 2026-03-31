package com.restaurant.td5springboot.repository;

import com.restaurant.td5springboot.entity.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientRepository {

    private final DataSource dataSource;

    public IngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Ingredient> findAll() {
        String sql = """
                SELECT id, name, price, category
                FROM Ingredient
                ORDER BY id
                """;

        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setPrice(rs.getDouble("price"));
                ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                ingredients.add(ingredient);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ingredients;
    }

    public Ingredient findIngredientById(int idIngredient) {
        String sql = """
                SELECT id, name, price, category
                FROM Ingredient
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idIngredient);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(rs.getInt("id"));
                    ingredient.setName(rs.getString("name"));
                    ingredient.setPrice(rs.getDouble("price"));
                    ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    return ingredient;
                } else {
                    throw new RuntimeException("Ingredient.id=" + idIngredient + " is not found");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public StockValue findStockValueAt(int idIngredient, Instant at) {
        String sql = """
                SELECT
                    SUM(CASE WHEN type = 'IN' THEN quantity ELSE -quantity END) as current_stock
                FROM stockmovement
                WHERE id_ingredient = ?
                    AND creation_datetime <= ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idIngredient);
            ps.setTimestamp(2, Timestamp.from(at));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StockValue stockValue = new StockValue();
                    stockValue.setQuantity(rs.getDouble("current_stock"));
                    return stockValue;
                } else {
                    throw new RuntimeException("No stock found for Ingredient.id=" + idIngredient);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findAllByIds(List<Integer> ids) {
        String sql = """
                SELECT id, name, price, category
                FROM Ingredient
                WHERE id = ANY(?)
                """;

        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            Array array = connection.createArrayOf("integer", ids.toArray());
            ps.setArray(1, array);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(rs.getInt("id"));
                    ingredient.setName(rs.getString("name"));
                    ingredient.setPrice(rs.getDouble("price"));
                    ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    ingredients.add(ingredient);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ingredients;
    }
}