select  * from dish;
select  * from dishingredient;
SELECT d.id as dish_id, d.name as dish_name, d.price, d.dish_type,
       i.id as ingredient_id, i.name as ingredient_name,
       i.category, i.price as ingredient_price
FROM Dish d
         LEFT JOIN DishIngredient di ON d.id = di.id_dish
         LEFT JOIN Ingredient i ON di.id_ingredient = i.id
ORDER BY d.id