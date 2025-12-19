package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order create(Order order, int userId) {
        String create = "INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount) VALUES (?, now(), ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS)){

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, order.getAddress());
            preparedStatement.setString(3, order.getCity());
            preparedStatement.setString(4, order.getState());
            preparedStatement.setString(5, order.getZip());
            preparedStatement.setBigDecimal(6, order.getShippingAmount());
            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                if(resultSet.next()){
                    int resultId = resultSet.getInt(1);
                    order.setOrderId(resultId);
                }
            }
            return order;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
