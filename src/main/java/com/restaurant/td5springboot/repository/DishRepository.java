package com.restaurant.td5springboot.repository;

import com.restaurant.td5springboot.entity.CategoryEnum;
import com.restaurant.td5springboot.entity.Dish;
import com.restaurant.td5springboot.entity.DishTypeEnum;
import com.restaurant.td5springboot.entity.Ingredient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DishRepository {

    private final DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
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
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<>(dishMap.values());
    }

    public Dish findById(int id) {
        String sql = """
                SELECT d.id as dish_id, d.name as dish_name, d.price, d.dish_type
                FROM Dish d
                WHERE d.id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Dish(
                            rs.getInt("dish_id"),
                            rs.getString("dish_name"),
                            DishTypeEnum.valueOf(rs.getString("dish_type")),
                            rs.getDouble("price")
                    );
                } else {
                    throw new RuntimeException("Dish.id=" + id + " is not found");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish updateIngredients(int dishId, List<Ingredient> ingredients) {
        String deleteSql = "DELETE FROM DishIngredient WHERE id_dish = ?";
        String insertSql = """
                INSERT INTO DishIngredient (id_dish, id_ingredient, quantity_required, unit)
                VALUES (?, ?, ?, ?::unit_type)
                """;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try {

                try (PreparedStatement deletePs = connection.prepareStatement(deleteSql)) {
                    deletePs.setInt(1, dishId);
                    deletePs.executeUpdate();
                }


                try (PreparedStatement insertPs = connection.prepareStatement(insertSql)) {
                    for (Ingredient ingredient : ingredients) {
                        insertPs.setInt(1, dishId);
                        insertPs.setInt(2, ingredient.getId());
                        insertPs.setDouble(3, 0.0);
                        insertPs.setString(4, "KG");
                        insertPs.executeUpdate();
                    }
                }

                connection.commit();
                return findById(dishId);

            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findIngredientsByDishId(int dishId, String ingredientName, Double ingredientPriceAround) {

        StringBuilder sql = new StringBuilder("""
            SELECT i.id, i.name, i.price, i.category
            FROM Ingredient i
            JOIN DishIngredient di ON i.id = di.id_ingredient
            WHERE di.id_dish = ?
            """);

        // filtre optionnel ingredientName
        if (ingredientName != null) {
            sql.append(" AND i.name ILIKE ?");
        }

        // filtre optionnel ingredientPriceAround
        if (ingredientPriceAround != null) {
            sql.append(" AND i.price BETWEEN ? AND ?");
        }

        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, dishId);

            if (ingredientName != null) {
                ps.setString(paramIndex++, "%" + ingredientName + "%");
            }

            if (ingredientPriceAround != null) {
                ps.setDouble(paramIndex++, ingredientPriceAround - 50);
                ps.setDouble(paramIndex++, ingredientPriceAround + 50);
            }

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