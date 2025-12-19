package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderLineItemDao;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class MySqlOrderLineItemDao extends MySqlDaoBase implements OrderLineItemDao {

    public MySqlOrderLineItemDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public OrderLineItem create(OrderLineItem orderLineItem) {

        String create = """
                INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount)
                Values (?, ?, ?, ?, ?)
                """;

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, orderLineItem.getOrderId());
            preparedStatement.setInt(2, orderLineItem.getProductId());
            preparedStatement.setBigDecimal(3, orderLineItem.getSalesPrice());
            preparedStatement.setInt(4, orderLineItem.getQuantity());
            preparedStatement.setBigDecimal(5, orderLineItem.getDiscount());
            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                if(resultSet.next()){
                    int resultId = resultSet.getInt(1);
                    orderLineItem.setOrderLineIdItem(resultId);
                }
            }
            return orderLineItem;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
