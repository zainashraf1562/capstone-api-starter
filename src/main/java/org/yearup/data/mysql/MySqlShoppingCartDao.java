package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource){
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        String getById = """
                SELECT * FROM shopping_cart
                JOIN products ON products.product_id = shopping_cart.product_id
                WHERE user_id = ?
                """;

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getById)){

            preparedStatement.setInt(1, userId);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    Product product = new Product(
                            resultSet.getInt("product_id"),
                            resultSet.getString("name"),
                            resultSet.getBigDecimal("price"),
                            resultSet.getInt("category_id"),
                            resultSet.getString("description"),
                            resultSet.getString("subcategory"),
                            resultSet.getInt("stock"),
                            resultSet.getBoolean("featured"),
                            resultSet.getString("image_url"));

                    ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                    shoppingCartItem.setProduct(product);
                    shoppingCartItem.setQuantity(resultSet.getInt("quantity"));
                    shoppingCartItem.setDiscountPercent(BigDecimal.ZERO);
                    cart.add(shoppingCartItem);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }

    @Override
    public ShoppingCart addToCart(int userId, int productId) {
        String updatingCart = """
                UPDATE shopping_cart
                SET quantity = quantity + 1
                WHERE user_id = ? AND product_id = ?;
                """;

        String addingToCart = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                        VALUES (?, ?, 1)
                """;

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updatingCart)){

            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,productId);

            int rows = preparedStatement.executeUpdate();

            if (rows == 0){
                try(PreparedStatement insertStatement = connection.prepareStatement(addingToCart)){
                    insertStatement.setInt(1, userId);
                    insertStatement.setInt(2, productId);
                    insertStatement.executeUpdate();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return getByUserId(userId);
    }

    @Override
    public void updateCart(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE product_id = ? AND user_id = ?";
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, productId);
            preparedStatement.setInt(3,userId);
            int updatedRows = preparedStatement.executeUpdate();

            if(updatedRows == 0) {
                throw new SQLException("Failed to Update!");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCart(int userId) {

    }

}
