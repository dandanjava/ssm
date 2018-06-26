package com.gbq.dao;

import java.util.List;

import com.gbq.po.Orders;
import com.gbq.po.User;

public interface OrdersMapper {
public List<Orders> findAllOrders();
public List<Orders> findOrders(Orders orders);
public List<Orders> findOrdersByIds(Orders orders);
//一对一关联
public List<Orders> ordersToUser();
//一对多关联
public List<User> userToOrders();
}
