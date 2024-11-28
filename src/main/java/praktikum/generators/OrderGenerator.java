package praktikum.generators;

import praktikum.models.Order;

public class OrderGenerator {
    public static Order createOrder() {
        String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa72"};
        return new Order(ingredients);
    }

    public static Order createOrderWithoutIngredients() {
        String[] ingredients = {};
        return new Order(ingredients);
    }

    public static Order createOrderWithInvalidHashIngredients() {
        String[] ingredients = {"Флюоресцентная булка R2-D3", "Биокотлета из марсианской Магнолии", "Соус Spicy-X"};
        return new Order(ingredients);
    }
}