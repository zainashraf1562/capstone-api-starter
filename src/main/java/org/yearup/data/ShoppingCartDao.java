package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    ShoppingCart addToCart(int userId, int productId);
    ShoppingCart updateCart(int userId, int productId, int quantity);
    void deleteCart(int userId);

    // add additional method signatures here

}
