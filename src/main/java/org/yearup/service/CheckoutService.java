package org.yearup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.models.*;

import java.math.BigDecimal;

@Component
public class CheckoutService {
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private ShoppingCartDao shoppingCartDao;
    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public CheckoutService(OrderDao orderDao, OrderLineItemDao orderLineItemDao, ShoppingCartDao shoppingCartDao, ProfileDao profileDao, UserDao userDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.shoppingCartDao = shoppingCartDao;
        this.profileDao = profileDao;
        this.userDao = userDao;
    }


    public Order checkout(String username) {
        User user = userDao.getByUserName(username);

        int userId = user.getId();
        Profile profile = profileDao.getProfileByUserId(userId);
        ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);

        Order order = buildOrder(userId, profile);
        Order newOrder = orderDao.create(order, userId);

        for (ShoppingCartItem item : shoppingCart.getItems().values()) {
            Product product = item.getProduct();

            OrderLineItem lineItem = new OrderLineItem(
                    newOrder.getOrderId(),
                    item.getProductId(),
                    product.getPrice(),
                    item.getQuantity(),
                    item.getDiscountPercent()
            );

            orderLineItemDao.create(lineItem);
        }

        shoppingCartDao.deleteCart(userId);
        return newOrder;
    }


    private Order buildOrder(int userId, Profile profile) {
        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(profile.getAddress());
        order.setCity(profile.getCity());
        order.setState(profile.getState());
        order.setZip(profile.getZip());
        order.setShippingAmount(BigDecimal.ZERO);
        return order;
    }






}
