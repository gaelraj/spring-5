select * from ingredient;
select  * from dish;
select  * from dishingredient;
SELECT d.id as dish_id, d.name as dish_name, d.price, d.dish_type,
       i.id as ingredient_id, i.name as ingredient_name,
       i.category, i.price as ingredient_price
FROM Dish d
         LEFT JOIN DishIngredient di ON d.id = di.id_dish
         LEFT JOIN Ingredient i ON di.id_ingredient = i.id
ORDER BY d.id;

SELECT id, name, price, category
FROM Ingredient
WHERE id = ANY(ARRAY[1,2,3,50]);

INSERT INTO DishIngredient (id_dish, id_ingredient, quantity_required, unit) VALUES (4, 1, 0, 'KG')